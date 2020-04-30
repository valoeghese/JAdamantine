package jadamantine.impl;

import com.google.gson.annotations.SerializedName;

class JSONMethodEntry {
	@SerializedName("name")
	String descriptor;
	@SerializedName("code")
	String[] code;
	@SerializedName("exceptions")
	public String[] exceptions;

	@Override
	public String toString() {
		return this.descriptor;
	}
}
