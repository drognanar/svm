package com.lexicalscope.svm.j.instruction.symbolic.symbols;


public class FalseSymbol implements BoolSymbol {
   public static FalseSymbol FF = new FalseSymbol();

   private FalseSymbol() { }

   @Override public boolean isTT() {
      return false;
   }

   @Override public boolean isFF() {
      return true;
   }

   @Override public <T, E extends Throwable> T accept(final SymbolVisitor<T, E> visitor) throws E {
      return visitor.fals3();
   }

   @Override public String toString() {
      return "FF";
   }

   @Override public BoolSymbol and(final BoolSymbol conjunct) {
      return this;
   }

   @Override public BoolSymbol or(final BoolSymbol disjunct) {
      return disjunct;
   }

   @Override public BoolSymbol not() {
      return TrueSymbol.TT;
   }
}
