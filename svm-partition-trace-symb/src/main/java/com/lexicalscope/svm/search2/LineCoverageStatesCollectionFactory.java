package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.search.Randomiser;

public class LineCoverageStatesCollectionFactory implements StatesCollectionFactory {
   @Override public StatesCollection statesCollection(final Randomiser randomiser, final TraceTreeSideObserver listener) {
      return new LineCoverageStatesCollection(listener);
   }
}
