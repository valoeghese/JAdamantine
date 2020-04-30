package jadamantine.api.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jadamantine.api.ICompiler;

/**
 * Class specifying an implementation of the API.
 */
public abstract class Implementation {
	/**
	 * @return a new, unique {@link ICompiler} instance.
	 */
	public abstract ICompiler createUniqueCompiler();
	/**
	 * @return the last {@link ICompiler} created with {@link Implementation#getOrCreateCompiler()}, but not with {@link Implementation#createUniqueCompiler()}, if it exists. Otherwise create a new compiler.
	 */
	public abstract ICompiler getOrCreateCompiler();

	private static Implementation IMPL;

	public static final void set(@Nonnull Implementation impl) {
		IMPL = impl;
	}

	@Nullable
	public static final Implementation get() {
		return IMPL;
	}
}
