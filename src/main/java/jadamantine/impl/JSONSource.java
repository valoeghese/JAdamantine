package jadamantine.impl;

import com.google.gson.annotations.SerializedName;

class JSONSource {
	@SerializedName("package")
	String classPackage;
	@SerializedName("super_class")
	String parent = "java.lang.Object";
	@SerializedName("access")
	String access = "";
	@SerializedName("outer_class")
	String outerClass = null;
}
