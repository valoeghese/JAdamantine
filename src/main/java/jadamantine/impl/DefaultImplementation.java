package jadamantine.impl;

import jadamantine.api.ICompiler;
import jadamantine.api.impl.Implementation;

public class DefaultImplementation extends Implementation {
	@Override
	public ICompiler getOrCreateCompiler() {
		return this.currentCompiler == null ? this.currentCompiler = new Compiler() : this.currentCompiler;
	}

	private ICompiler currentCompiler;

	@Override
	public ICompiler createUniqueCompiler() {
		return new Compiler();
	}
}
