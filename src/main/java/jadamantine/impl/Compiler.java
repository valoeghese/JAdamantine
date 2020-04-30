package jadamantine.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.math3.util.Pair;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import jadamantine.api.ICompiler;
import jadamantine.api.VerboseOutput;

class Compiler implements ICompiler {
	private void searchForJSONSource(File file, Consumer<File> callback) {
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				this.searchForJSONSource(f, callback);
			} else if (f.getName().endsWith(".json")) {
				VerboseOutput.println("?~ Found potential json source file: " + f.getAbsolutePath());

				try {
					JSONValidator validator = GSON.fromJson(
							Files.newBufferedReader(f.toPath()),
							JSONValidator.class);

					if (validator.sourceFlag > 0) {
						VerboseOutput.println("s~ Validated as json source file: " + f.getAbsolutePath());
						callback.accept(f);
					}
				} catch (IOException e) {
					VerboseOutput.printErr("IOException while trying to validate source file", e);
				}
			}
		}
	}

	private static final Gson GSON = new Gson();

	@Override
	public void compile(File root, File outputRoot, boolean verbose) {
		VerboseOutput.setEnabled(verbose);

		List<File> jsonFiles = new ArrayList<>();
		System.out.println("i~ Searching for JSON source files in directory " + root.getAbsolutePath());
		this.searchForJSONSource(root, jsonFiles::add);

		if (jsonFiles.isEmpty()) {
			System.out.println("!~ Found no source files. Skipping compile stage.");
		} else {
			System.out.println("i~ Compiling found JSON source files");

			jsonFiles.stream().map(f -> {
				try {
					return new Pair<>(
							f.getName().substring(0, f.getName().length() - 5),
							GSON.fromJson(
									Files.newBufferedReader(f.toPath()),
									JSONSource.class
									));
				} catch (IOException e) {
					VerboseOutput.printErr("IOException while attempting to compile source file " + f.getAbsolutePath(), e);
					return null;
				}
			}).filter(Filters::nonNull).forEach(classPrimer -> {
				VerboseOutput.println("i~ Compiling class " + classPrimer.getFirst());
				JSONSource source = classPrimer.getSecond();
				ClassNode output = new ClassNode(Opcodes.ASM8);
				output.version = Opcodes.V1_8;
				output.name = source.classPackage + "/" + classPrimer.getFirst();
				output.superName = source.parent;
				output.access = StringToASM.accessModifiers(source.access.split("\\s"));

				if (output.outerClass != null) {
					output.outerClass = source.outerClass;
				}

				VerboseOutput.println("i~ Adding fields.");
				for (String field : source.fields) {
					VerboseOutput.println("i~ Adding field " + field);
					output.fields.add(createField(field));
				}

				VerboseOutput.println("i~ Adding methods.");
				for (JSONMethodEntry method : source.methods) {
					VerboseOutput.println("i~ Adding method " + method);
					output.methods.add(createMethod(method, output.name));
				}

				VerboseOutput.println("i~ Outputting to File.");
				File fileOut = new File(outputRoot.getPath() + "/" + output.name + ".class");
				fileOut.getAbsoluteFile().getParentFile().mkdirs();

				try {
					fileOut.createNewFile();
					ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
					output.accept(writer);

					try(FileOutputStream fos = new FileOutputStream(fileOut)) {
						fos.write(writer.toByteArray());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private static FieldNode createField(String fieldEntry) {
		String[] fieldData = fieldEntry.split("\\s");
		int i = 0;
		int accumulativeIndex = 0;
		int temp;
		int access = 0;

		while ((temp = StringToASM.accessModifier(fieldData[i])) != 0) {
			access |= temp;
			accumulativeIndex += fieldData[i].length() + 1;
			++i;
		}

		String name = fieldData[i++];
		accumulativeIndex += name.length() + 1;
		String descriptor = fieldData[i++];
		accumulativeIndex += descriptor.length() + 1;

		String valueRaw = i == fieldData.length ? null : fieldEntry.substring(accumulativeIndex);
		Object value = valueRaw == null ? pickDefault(descriptor) : valueOf(descriptor, valueRaw);

		return new FieldNode(Opcodes.ASM8, access, name, descriptor, descriptor, value);
	}

	private static MethodNode createMethod(JSONMethodEntry method, String clazz) {
		String methodDescriptor[] = method.descriptor.split("\\s");
		Map<String, Label> labels = Maps.newHashMap();

		int i = -1;
		int temp;
		int access = 0;

		while ((temp = StringToASM.accessModifier(methodDescriptor[++i])) != 0) {
			access |= temp;
		}

		String descriptorRaw = methodDescriptor[i];
		i = descriptorRaw.indexOf("(");
		String name = descriptorRaw.substring(0, i);
		String descriptor = descriptorRaw.substring(i);

		MethodNode result = new MethodNode(Opcodes.ASM8, access, name, descriptor, descriptor, method.exceptions);

		for (String instruction : method.code) {
			StringToASM.instruction(result, clazz, instruction, labels);
		}

		return result;
	}

	private static Object pickDefault(String descriptor) {
		switch (descriptor) {
		case "B":
			return (byte) 0;
		case "S":
			return (short) 0;
		case "I":
			return 0;
		case "J":
			return 0L;
		case "F":
			return 0f;
		case "D":
			return 0.0;
		case "Z":
			return false;
		case "C":
			return '\u0000';
		default:
			return null;
		}
	}

	private static Object valueOf(String descriptor, String value) {
		switch (descriptor) {
		case "B":
			return Byte.parseByte(value);
		case "S":
			return Short.parseShort(value);
		case "I":
			return Integer.parseInt(value);
		case "J":
			return Long.parseLong(value);
		case "F":
			return Float.parseFloat(value);
		case "D":
			return Double.parseDouble(value);
		case "Z":
			return Boolean.parseBoolean(value);
		case "C":
			return (char) Integer.parseInt(value);
		case "Ljava/lang/String;":
			return value;
		default:
			throw new UnsupportedOperationException("Because of ASM limitations, do not provide default values for objects in the fields. Instead, use the <clinit> method.");
		}
	}
}
