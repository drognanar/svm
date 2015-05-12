package com.lexicalscope.svm.j.instruction.symbolic.symbols;


public abstract class AbstractBoolSymbol implements BoolSymbol {
   @Override public boolean isTT() {
      return false;
   }

   @Override public boolean isFF() {
      return false;
   }

   @Override public final BoolSymbol and(final BoolSymbol conjunct) {
      if(conjunct instanceof TrueSymbol) {
         return this;
      } else if (conjunct instanceof FalseSymbol) {
         return conjunct;
      }
      return new AndSymbol(this, conjunct);
   }

   @Override public final BoolSymbol or(final BoolSymbol disjunct) {
      if(disjunct instanceof TrueSymbol) {
         return disjunct;
      } else if (disjunct instanceof FalseSymbol) {
         return this;
      }
      return new OrSymbol(this, disjunct);
   }

   @Override public final BoolSymbol not() {
      return new NotSymbol(this);
   }
}