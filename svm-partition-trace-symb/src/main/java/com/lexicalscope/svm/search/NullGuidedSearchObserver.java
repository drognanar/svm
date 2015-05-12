package com.lexicalscope.svm.search;

import com.lexicalscope.svm.search2.Side;
import com.lexicalscope.svm.vm.j.JState;

public class NullGuidedSearchObserver implements GuidedSearchObserver {
   @Override public void picked(final JState pending, final GuidedSearchState side) { }
   @Override public void goal(final JState pending) { }
   @Override public void leaf(final JState pending) { }
   @Override public void forkAt(final JState parent) { }
   @Override public void picked(final JState pending, final Side currentSide) { }
}
