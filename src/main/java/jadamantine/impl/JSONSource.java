package jadamantine.impl;

import com.google.gson.annotations.SerializedName;

class JSONSource {
	@SerializedName("package")
	String classPackage;
	@SerializedName("super_class")
	String parent = "java/lang/Object";
	@SerializedName("access")
	String access = "";
	@SerializedName("outer_class")
	String outerClass = null;
	@SerializedName("fields")
	String fields[] = new String[0];
	@SerializedName("methods")
	JSONMethodEntry methods[] = new JSONMethodEntry[0];
}
