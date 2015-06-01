package com.lexicalscope.svm.j.instruction.symbolic.symbols;

public class OrSymbol extends AbstractBoolSymbol {
   private final BoolSymbol left;
   private final BoolSymbol right;

   public OrSymbol(final BoolSymbol left, final BoolSymbol right) {
      this.left = left;
      this.right = right;
   }

   @Override public <T, E extends Throwable> T accept(final SymbolVisitor<T, E> visitor) throws E {
      return visitor.or(left, right);
   }

   @Override public String toString() {
      return String.format("(OR %s %s)", left, right);
   }

   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (left == null ? 0 : left.hashCode());
      result = prime * result + (right == null ? 0 : right.hashCode());
      return result;
   }

   @Override public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final OrSymbol other = (OrSymbol) obj;
      if (left == null) {
         if (other.left != null) {
            return false;
         }
      } else if (!left.equals(other.left)) {
         return false;
      }
      if (right == null) {
         if (other.right != null) {
            return false;
         }
      } else if (!right.equals(other.right)) {
         return false;
      }
      return true;
   }
}
