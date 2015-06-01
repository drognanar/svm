package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.search.Randomiser;

public interface StatesCollectionFactory {
   StatesCollection statesCollection(Randomiser randomiser, TraceTreeSideObserver listener);
}
