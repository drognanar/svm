package com.lexicalscope.svm.z3;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IConstSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;

public class ViolationModel {
   private final SModel powerSimplify;

   public ViolationModel(final SModel powerSimplify) {
      this.powerSimplify = powerSimplify;
   }

   public int[] modelForBv32Array(final IArrayAndLengthSymbols symbols) {
      return powerSimplify.funcInterp(symbols);
   }

   public int intModelForBv32Expr(final ISymbol symbol) {
      return ((IConstSymbol) powerSimplify.interp(symbol)).val();
   }
}
