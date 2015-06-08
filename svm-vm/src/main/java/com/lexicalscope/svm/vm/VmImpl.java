package com.lexicalscope.svm.vm;

import java.util.Collection;

public final class VmImpl<S extends VmState> implements Vm<S> {
   private final StateSearch<S> search;
   private SearchLimits limits;

	public VmImpl(final StateSearch<S> search) {
      this(search, new StateCountSearchLimit());
   }

	public VmImpl(final StateSearch<S> search, final SearchLimits limits) {
	   this.search = search;
      this.limits = limits;
	}

   @Override
	public S execute() {
	   limits.reset();
      S pending;
      while ((pending = search.pendingState()) != null && limits.withinLimits()) {
			try {
			   pending.eval();
			} catch (final TerminationException termination) {
			} catch (final RuntimeException e) {
				throw e;
			}
			limits.searchedState();
		}
		return search.firstResult();
	}

	@Override
	public S result() {
		return search.firstResult();
	}

	@Override
	public Collection<S> results() {
		return search.results();
	}
}
