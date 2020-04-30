package jadamantine.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.util.Pair;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

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
				JSONSource source = classPrimer.getSecond();
				ClassNode output = new ClassNode(Opcodes.ASM8);
				output.version = Opcodes.V1_8; // idk what this number is for lol
				output.name = classPrimer.getFirst();
				output.superName = source.parent;
				output.access = StringToASM.accessModifiers(source.access.split("\\s"));

				if (output.outerClass != null) {
					output.outerClass = source.outerClass;
				}
			});
		}
	}
}
