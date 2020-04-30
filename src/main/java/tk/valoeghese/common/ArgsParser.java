package tk.valoeghese.common;

import java.util.HashMap;
import java.util.Map;

public final class ArgsParser {
	public static <T extends IProgramArgs> T of(String[] args, T base) {
		Map<String, String> valueMap = new HashMap<>();
		boolean valueFlag = false;
		String keyCache = "";

		for (String s : args) {

			if (valueFlag) {
				valueFlag = false;
				valueMap.put(keyCache, s);
			} else {

				if (s.startsWith("-")) {

					if (s.startsWith("--")) {
						valueMap.put(s.substring(2), "true");
					} else {
						valueFlag = true;
						keyCache = s.substring(1);
					}
				}
			}
		}

		base.setArgs(new ArgsData(valueMap));
		return base;
	}

	public static <T extends IProgramArgs> T of(String[] args, String[] extraArgs, T base) {
		Map<String, String> valueMap = new HashMap<>();
		boolean valueFlag = false;
		String keyCache = "";

		for (String s : args) {
			if (valueFlag) {
				valueFlag = false;
				valueMap.put(keyCache, s);
			} else {

				if (s.startsWith("-")) {

					if (s.startsWith("--")) {
						valueMap.put(s.substring(2), "true");
					} else {
						valueFlag = true;
						keyCache = s.substring(1);
					}
				}
			}
		}

		for (String s : extraArgs) {
			if (valueFlag) {
				valueFlag = false;
				valueMap.put(keyCache, s);
			} else {

				if (s.startsWith("-")) {

					if (s.startsWith("--")) {
						valueMap.put(s.substring(2), "true");
					} else {
						valueFlag = true;
						keyCache = s.substring(1);
					}
				}
			}
		}

		base.setArgs(new ArgsData(valueMap));
		return base;
	}
}
