package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class NullMismatchReport implements MismatchReport {
   @Override public void mismatch(final FeasibilityChecker checker, final Trace nodeTraceP, final BoolSymbol pPc, final Trace nodeTraceQ, final BoolSymbol qPc) { }
}
