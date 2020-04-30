package jadamantine.api;

/**
 * Standard:
 * <ul>
 * <li> i~	:	info
 * <li> s~	:	success
 * <li> ?~	:	potential success or fail operation (discovery)
 * <li> !~	:	warn
 * <li> X~	:	error
 * <li> [any of the above codes] A Title: /~<br/>
 *      content<br/>
 *      ~/	:	Multiline
 * </ul>
 */
public class VerboseOutput {
	public static void setEnabled(boolean verbose) {
		enabled = verbose;
	}

	private static boolean enabled;

	public static <T> void println(T t) {
		if (enabled) {
			System.out.println(t);
		}
	}

	public static void printErr(String title, Throwable t) {
		if (enabled) {
			println("X~ " + title + ": /~");
			t.printStackTrace();
			println("~/");
		}
	}
}