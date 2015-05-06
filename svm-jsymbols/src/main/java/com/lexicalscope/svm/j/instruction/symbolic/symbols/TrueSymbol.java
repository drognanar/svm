package com.lexicalscope.svm.j.instruction.symbolic.symbols;


public final class TrueSymbol implements BoolSymbol {
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
      return new FalseSymbol();
   }

   @Override public String toString() {
      return "TT";
   }

   @Override public int hashCode() {
      return TrueSymbol.class.hashCode();
   }

   @Override public boolean equals(final Object obj) {
      return obj != null && obj.getClass().equals(this.getClass());
   }
}
