package com.lexicalscope.svm.j.instruction.symbolic.predicates;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.FalseSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.GtSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;

public final class GtStrategy implements UnarySBranchOp {
   @Override public BoolSymbol conditionSymbol(final ISymbol operand) {
      return new GtSymbol(operand);
   }

   @Override public BoolSymbol conditionSymbol(final Integer value) {
      if(value > 0) {
         return TrueSymbol.TT;
      }
      return FalseSymbol.FF;
   }

   @Override public String toString() {
      return "IFGT";
   }
}