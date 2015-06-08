package com.lexicalscope.svm.partition.trace.symb.search2;

import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.search2.LineCoverageStatesCollection;
import com.lexicalscope.svm.search2.StatesCollection;
import com.lexicalscope.svm.search2.StatesCollectionFactory;
import com.lexicalscope.svm.search2.TraceTreeSideObserver;

public class ArrayListCollectionFactory implements StatesCollectionFactory {
    @Override public StatesCollection statesCollection(final Randomiser randomiser, final TraceTreeSideObserver listener) {
        return new ArrayListStatesCollection(randomiser, listener);
    }
}

