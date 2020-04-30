package jadamantine;

import java.io.File;

import jadamantine.api.ICompiler;
import jadamantine.api.impl.Implementation;
import jadamantine.impl.DefaultImplementation;
import tk.valoeghese.common.ArgsData;
import tk.valoeghese.common.ArgsParser;
import tk.valoeghese.common.IProgramArgs;

public class CompilerMain {
	public static void main(String[] cmdArgs) {
		// set implementation
		Implementation.set(new DefaultImplementation());
		// start
		args = ArgsParser.of(cmdArgs, new Args());
		ICompiler.getOrCreate().compile(new File(args.rootDir), new File(args.outputDir), args.verbose);
	}

	public static Args args;

	public static class Args implements IProgramArgs {
		@Override
		public void setArgs(ArgsData args) {
			this.rootDir = args.getString("srcDir", () -> args.getStringOrDefault("s", "src"));
			this.outputDir = args.getString("outputDir", () -> args.getStringOrDefault("o", "bin"));
			this.verbose = args.getBoolean("verbose") || args.getBoolean("v");
		}

		private String rootDir;
		private String outputDir;
		public boolean verbose;
	}
}
