package jadamantine.impl;

import java.util.ArrayList;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

class StringToASM {
	static int accessModifiers(String[] access) {
		int result = 0;

		for (String item : access) {
			if (!item.isEmpty()) {
				result |= accessModifier(item);
			}
		}
		return result;
	}

	static int accessModifier(String item) {
		switch (item.toUpperCase()) {
		case "PUBLIC": // access width
			return Opcodes.ACC_PUBLIC;
		case "PRIVATE":
			return Opcodes.ACC_PRIVATE;
		case "PROTECTED":
			return Opcodes.ACC_PROTECTED;
		case "STATIC": // access type
			return Opcodes.ACC_STATIC;
		case "FINAL":
			return Opcodes.ACC_FINAL;
		case "SYNCHRONIZED":
			return Opcodes.ACC_SYNCHRONIZED;
		case "TRANSIENT":
			return Opcodes.ACC_TRANSIENT;
		case "ENUM": // type
			return Opcodes.ACC_ENUM;
		case "INTERFACE":
			return Opcodes.ACC_INTERFACE;
		case "ANNOTATION":
			return Opcodes.ACC_ANNOTATION;
		case "SYNTHETIC": // other
			return Opcodes.ACC_SYNTHETIC;
		case "BRIDGE":
			return Opcodes.ACC_BRIDGE;
		case "DEPRECATED":
			return Opcodes.ACC_DEPRECATED;
		case "ABSTRACT":
			return Opcodes.ACC_ABSTRACT;
		case "TRANSITIVE":
			return Opcodes.ACC_TRANSITIVE;
		default:
			return 0;
		}
	}

	static void instruction(MethodNode method, String clazz, String instruction, Map<String, Label> labels) {
		String data[] = instruction.split("\\s");
		String methodData[];
		String type = data[0];

		switch (type.toUpperCase()) {
		case "ILOAD": // load int value from local variable
			method.visitVarInsn(Opcodes.ILOAD, Integer.parseInt(data[1]));
			break;
		case "ALOAD": // load object reference from local variable
			method.visitVarInsn(Opcodes.ALOAD, Integer.parseInt(data[1]));
			break;
		case "ICONST_M1": // load int constant -1
			method.visitInsn(Opcodes.ICONST_M1);
			break;
		case "ICONST_0": // load int constant 0
			method.visitInsn(Opcodes.ICONST_0);
			break;
		case "ICONST_1": // load int constant 1
			method.visitInsn(Opcodes.ICONST_1);
			break;
		case "ICONST_2": // load int constant 2
			method.visitInsn(Opcodes.ICONST_2);
			break;
		case "ICONST_3": // load int constant 3
			method.visitInsn(Opcodes.ICONST_3);
			break;
		case "ICONST_4": // load int constant 4
			method.visitInsn(Opcodes.ICONST_4);
			break;
		case "ICONST_5": // load int constant 5
			method.visitInsn(Opcodes.ICONST_5);
			break;
		case "BIPUSH": // push byte
			method.visitIntInsn(Opcodes.BIPUSH, Integer.parseInt(data[1]));
			break;
		case "SIPUSH": // push short
			method.visitIntInsn(Opcodes.SIPUSH, Integer.parseInt(data[1]));
			break;
		case "LDC": // push constant from declared pool
			method.visitIntInsn(Opcodes.LDC, Integer.parseInt(data[1]));
			break;
		case "LDC_I": // push int constant from declared pool
			method.visitLdcInsn(Integer.parseInt(data[1]));
			break;
		case "ISTORE": // store int in variable
			method.visitVarInsn(Opcodes.ISTORE, Integer.parseInt(data[1]));
			break;
		case "ASTORE": // store object reference in variable
			method.visitVarInsn(Opcodes.ASTORE, Integer.parseInt(data[1]));
			break;
		case "LABEL": // create a label at this location
			Label label = new Label();
			labels.put(data[1], label);
			method.visitLabel(label);
			break;
		case "GOTO": // go to label
			method.visitJumpInsn(Opcodes.GOTO, labels.get(data[1]));
			break;
		case "INVOKESTATIC": // static method
			methodData = splitDescriptor(data[1]);
			method.visitMethodInsn(Opcodes.INVOKESTATIC, methodData[0], methodData[1], methodData[2], Boolean.parseBoolean(data[2]));
			break;
		case "RETURN": // return
			switch(method.desc.charAt(method.desc.length() - 1)) {
			case ';':
				method.visitInsn(Opcodes.ARETURN);
				break;
			case 'F':
				method.visitInsn(Opcodes.FRETURN);
				break;
			case 'D':
				method.visitInsn(Opcodes.DRETURN);
				break;
			case 'V':
				method.visitInsn(Opcodes.RETURN);
				break;
			case 'J':
				method.visitInsn(Opcodes.LRETURN);
				break;
			default:
				method.visitInsn(Opcodes.IRETURN);
				break;
			}
			break;
		case "NEW": // new type
			method.visitTypeInsn(Opcodes.NEW, data[1]);
			break;
		case "DUP": // duplicate top of stack
			method.visitInsn(Opcodes.DUP);
			break;
		case "INVOKESPECIAL": // special method
			methodData = splitDescriptor(data[1]);
			method.visitMethodInsn(Opcodes.INVOKESPECIAL, methodData[0], methodData[1], methodData[2], Boolean.parseBoolean(data[2]));
			break;
		case "INVOKECONSTRUCTOR": // added for json thing: utility for constructors.
			methodData = splitDescriptor(data[1]);
			method.visitTypeInsn(Opcodes.NEW, methodData[0]);
			method.visitInsn(Opcodes.DUP);
			method.visitMethodInsn(Opcodes.INVOKESPECIAL, methodData[0], methodData[1], methodData[2], false);
			break;
		case "PUTSTATIC": // put in static field
			method.visitFieldInsn(Opcodes.PUTSTATIC, clazz, data[1], data[2]);
			break;
		case "GETSTATIC": // get from static field
			method.visitFieldInsn(Opcodes.GETSTATIC, clazz, data[1], data[2]);
			break;
		case "INVOKEINTERFACE": // invoke interface method
			methodData = splitDescriptor(data[1]);
			method.visitMethodInsn(Opcodes.INVOKEINTERFACE, methodData[0], methodData[1], methodData[2], true);
			break;
		case "INVOKEVIRTUAL": // invoke instance method
			methodData = splitDescriptor(data[1]);
			method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, methodData[0], methodData[1], methodData[2], data.length == 2 ? false : Boolean.parseBoolean(data[2]));
			break;
		}
	}

	private static String[] splitDescriptor(String combinedDescriptor) {
		int i = combinedDescriptor.indexOf('(');
		int j = i;
		char[] characters = combinedDescriptor.toCharArray();

		while (true) {
			if (characters[--j] == '.') {
				break;
			}
		}

		return new String[] {
				combinedDescriptor.substring(0, j),
				combinedDescriptor.substring(j + 1, i),
				combinedDescriptor.substring(i)
		};
	}
}
