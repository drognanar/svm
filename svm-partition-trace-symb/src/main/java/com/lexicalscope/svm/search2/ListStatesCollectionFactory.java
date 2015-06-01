package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.search.Randomiser;

public class ListStatesCollectionFactory implements StatesCollectionFactory {
   @Override public StatesCollection statesCollection(final Randomiser randomiser, final TraceTreeSideObserver listener) {
      return new ListStatesCollection(randomiser, listener);
   }
}
