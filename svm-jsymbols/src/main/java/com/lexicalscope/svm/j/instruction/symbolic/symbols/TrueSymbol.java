package com.lexicalscope.svm.j.instruction.symbolic.symbols;


public final class TrueSymbol implements BoolSymbol {
   public static TrueSymbol TT = new TrueSymbol();

   private TrueSymbol() {}

   @Override public boolean isTT() {
      return true;
   }

   @Override public boolean isFF() {
      return false;
   }

   @Override public <T, E extends Throwable> T accept(final SymbolVisitor<T, E> visitor) throws E {
      return visitor.tru3();
   }

   @Override public BoolSymbol and(final BoolSymbol conjunct) {
      return conjunct;
   }

   @Override public BoolSymbol or(final BoolSymbol disjunct) {
      return this;
   }

   @Override public BoolSymbol not() {
      return FalseSymbol.FF;
   }

   @Override public String toString() {
      return "TT";
   }
}
