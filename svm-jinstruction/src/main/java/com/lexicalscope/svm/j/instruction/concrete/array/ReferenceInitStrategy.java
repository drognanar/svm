package com.lexicalscope.svm.j.instruction.concrete.array;

import com.lexicalscope.symb.vm.State;

final class ReferenceInitStrategy implements InitStrategy {
   @Override public Object initialValue(final State ctx) {
      return ctx.nullPointer();
   }
}