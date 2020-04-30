package jadamantine.api;

import java.io.File;

import javax.annotation.Nonnull;

import jadamantine.NonNullWithImplementation;
import jadamantine.api.impl.Implementation;

/**
 * Interface representing a compiler.
 */
public interface ICompiler {
	/**
	 * @param root the root directory of the source file inputs.
	 * @param outputRoot the root directory to which to output.
	 * @param verbose whether to be verbose in the console.
	 */
	void compile(@Nonnull File root, @Nonnull File outputRoot, boolean verbose);

	/**
	 * @return a new, unique {@link ICompiler} instance.
	 */
	@NonNullWithImplementation
	static ICompiler createNew() {
		return Implementation.get().createUniqueCompiler();
	}

	/**
	 * @return the current {@link ICompiler} instance, or a new one if one is not current.
	 * @implNote this will never return a compiler created with {@link ICompiler#createNew()}.
	 */
	@NonNullWithImplementation
	static ICompiler getOrCreate() {
		return Implementation.get().getOrCreateCompiler();
	}
}
