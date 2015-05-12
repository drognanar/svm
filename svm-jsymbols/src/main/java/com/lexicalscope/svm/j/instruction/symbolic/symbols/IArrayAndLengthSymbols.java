package com.lexicalscope.svm.j.instruction.symbolic.symbols;

public class IArrayAndLengthSymbols {
   private final IArraySymbol arraySymbol;
   private final ISymbol lengthSymbol;

   public IArrayAndLengthSymbols(final IArraySymbol arraySymbol, final ISymbol lengthSymbol) {
      this.arraySymbol = arraySymbol;
      this.lengthSymbol = lengthSymbol;
   }

   public IArraySymbol getArraySymbol() {
      return arraySymbol;
   }

   public ISymbol getLengthSymbol() {
      return lengthSymbol;
   }
}

