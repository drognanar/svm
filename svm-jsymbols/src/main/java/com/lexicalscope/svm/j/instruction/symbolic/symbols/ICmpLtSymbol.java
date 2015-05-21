package com.lexicalscope.svm.j.instruction.symbolic.symbols;

public class ICmpLtSymbol extends AbstractBoolSymbol {
   private final ISymbol value1;
   private final ISymbol value2;

   public ICmpLtSymbol(final ISymbol value1, final ISymbol value2) {
      this.value1 = value1;
      this.value2 = value2;
   }

   @Override public <T, E extends Throwable> T accept(final SymbolVisitor<T, E> visitor) throws E {
      return visitor.lt(value1, value2);
   }

   @Override
   public String toString() {
      return String.format("(< %s %s)", value1, value2);
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      final ICmpLtSymbol that = (ICmpLtSymbol) o;

      if (!value1.equals(that.value1)) {
         return false;
      }
      return value2.equals(that.value2);
   }

   @Override
   public int hashCode() {
      int result = value1.hashCode();
      result = 31 * result + value2.hashCode();
      return result;
   }
}
