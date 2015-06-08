package com.lexicalscope.svm.j.instruction.symbolic.predicates;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.FalseSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpGtSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;

public final class ICmpGtStrategy implements BinarySBranchOp {
   @Override public String toString() {
      return "ICMPGT";
   }

   @Override public BoolSymbol conditionSymbol(final ISymbol value1, final ISymbol value2) {
      return new ICmpGtSymbol(value1, value2);
   }

   @Override public BoolSymbol conditionSymbol(final Integer value1, final Integer value2) {
      if (value1 > value2) {
         return TrueSymbol.TT;
      }
      return FalseSymbol.FF;
   }
}