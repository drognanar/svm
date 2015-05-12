package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public interface MismatchReport {
   /**
    * Overlapping path conditions produced different traces
    *
    * @param checker the checker that can be used to explore the mismatch
    * @param nodeTraceP the trace that side p followed
    * @param pPc the path condition that reached the p trace
    * @param nodeTraceQ the trace that side q followed
    * @param qPc the path condition that reached the q trace
    */
   void mismatch(FeasibilityChecker checker, Trace nodeTraceP, BoolSymbol pPc, Trace nodeTraceQ, BoolSymbol qPc);
}
