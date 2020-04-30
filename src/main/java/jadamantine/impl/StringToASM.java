package jadamantine.impl;

import org.objectweb.asm.Opcodes;

class StringToASM {
	static int accessModifiers(String[] access) {
		int result = 0;

		for (String item : access) {
			if (!item.isEmpty()) {
				switch (item.toUpperCase()) {
				// access width
				case "PUBLIC":
					result |= Opcodes.ACC_PUBLIC;
					break;
				case "PRIVATE":
					result |= Opcodes.ACC_PRIVATE;
					break;
				case "PROTECTED":
					result |= Opcodes.ACC_PROTECTED;
					break;
				// access type
				case "STATIC":
					result |= Opcodes.ACC_STATIC;
					break;
				case "FINAL":
					result |= Opcodes.ACC_FINAL;
					break;
				case "SYNCHRONIZED":
					result |= Opcodes.ACC_SYNCHRONIZED;
					break;
				case "TRANSIENT":
					result |= Opcodes.ACC_TRANSIENT;
					break;
				// type
				case "ENUM":
					result |= Opcodes.ACC_ENUM;
					break;
				case "INTERFACE":
					result |= Opcodes.ACC_INTERFACE;
					break;
				case "ANNOTATION":
					result |= Opcodes.ACC_ANNOTATION;
					break;
				// other
				case "SYNTHETIC":
					result |= Opcodes.ACC_SYNTHETIC;
					break;
				case "BRIDGE":
					result |= Opcodes.ACC_BRIDGE;
					break;
				case "DEPRECATED":
					result |= Opcodes.ACC_DEPRECATED;
					break;
				case "ABSTRACT":
					result |= Opcodes.ACC_ABSTRACT;
					break;
				case "TRANSITIVE":
					result |= Opcodes.ACC_TRANSITIVE;
					break;
				}
			}
		}
		return result;
	}
}
