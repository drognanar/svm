package com.lexicalscope.svm.j.instruction.symbolic.predicates;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.icmpEq;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.FalseSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;

public final class ICmpEqStrategy implements BinarySBranchOp {
   @Override public String toString() {
      return "ICMPEQ";
   }

   @Override public BoolSymbol conditionSymbol(final ISymbol value1, final ISymbol value2) {
      return icmpEq(value1, value2);
   }

   @Override public BoolSymbol conditionSymbol(final Integer value1, final Integer value2) {
      return pConditionSymbol(value1, value2);
   }

   private BoolSymbol pConditionSymbol(final int value1, final int value2) {
      if (value1 == value2) {
         return TrueSymbol.TT;
      }
      return FalseSymbol.FF;
   }
}