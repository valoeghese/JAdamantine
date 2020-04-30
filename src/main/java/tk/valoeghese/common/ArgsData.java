package tk.valoeghese.common;

import java.util.Map;
import java.util.function.Supplier;

public class ArgsData {
	private final Map<String, String> valueMap;

	ArgsData(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}

	public boolean getBoolean(String key) {
		String result = this.valueMap.getOrDefault(key, "false");

		try {
			return Boolean.valueOf(result);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public String getStringOrDefault(String key, String defaultValue) {
		return this.valueMap.getOrDefault(key, defaultValue);
	}

	public String getString(String key, Supplier<String> ifNoSuchKey) {
		String result = this.valueMap.get(key);

		if (result == null) {
			return ifNoSuchKey.get();
		}

		return result;
	}
}