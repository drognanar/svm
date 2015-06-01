package com.lexicalscope.svm.search2;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.and;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.z3.FeasibilityChecker;
import com.lexicalscope.svm.z3.ViolationModel;

public class PartitionViolationException extends RuntimeException {
   private final Trace nodeTraceP;
   private final Trace nodeTraceQ;
   private final BoolSymbol pPc;
   private final BoolSymbol qPc;
   private final FeasibilityChecker checker;

   public PartitionViolationException(final FeasibilityChecker checker, final Trace nodeTraceP, final BoolSymbol pPc, final Trace nodeTraceQ, final BoolSymbol qPc) {
      super(violationDescription(nodeTraceP, pPc, nodeTraceQ, qPc));
      this.checker = checker;
      this.nodeTraceP = nodeTraceP;
      this.pPc = pPc;
      this.nodeTraceQ = nodeTraceQ;
      this.qPc = qPc;
   }

   private static String violationDescription(final Trace nodeTraceP, final BoolSymbol pPc, final Trace nodeTraceQ, final BoolSymbol qPc) {
      return String.format("P Path condition (%s) reached trace %s%n"
                         + "Q Path condition (%s) reached trace %s",
                         pPc,
                         nodeTraceP,
                         qPc,
                         nodeTraceQ);
   }

   public Trace getNodeTraceP() {
      return nodeTraceP;
   }

   public Trace getNodeTraceQ() {
      return nodeTraceQ;
   }

   public BoolSymbol pPc() {
      return pPc;
   }

   public BoolSymbol qPc() {
      return qPc;
   }

   public ViolationModel violationModel() {
      return checker.violationModel(and(pPc, qPc));
   }
}
