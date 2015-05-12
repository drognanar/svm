package com.lexicalscope.svm.search;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.vm.j.JState;

public interface SearchMetaExtractor {
   void configureInitial(JState state);
   Trace goal(JState state);
   BoolSymbol pc(JState state);
}
