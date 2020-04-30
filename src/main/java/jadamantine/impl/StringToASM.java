package jadamantine.impl;

import org.objectweb.asm.Opcodes;

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
		// access width
		case "PUBLIC":
			return Opcodes.ACC_PUBLIC;
		case "PRIVATE":
			return Opcodes.ACC_PRIVATE;
		case "PROTECTED":
			return Opcodes.ACC_PROTECTED;
		// access type
		case "STATIC":
			return Opcodes.ACC_STATIC;
		case "FINAL":
			return Opcodes.ACC_FINAL;
		case "SYNCHRONIZED":
			return Opcodes.ACC_SYNCHRONIZED;
		case "TRANSIENT":
			return Opcodes.ACC_TRANSIENT;
		// type
		case "ENUM":
			return Opcodes.ACC_ENUM;
		case "INTERFACE":
			return Opcodes.ACC_INTERFACE;
		case "ANNOTATION":
			return Opcodes.ACC_ANNOTATION;
		// other
		case "SYNTHETIC":
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
}
