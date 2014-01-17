package com.lexicalscope.symb.vm.instructions.ops;

import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.Vop;

public class AConstNullOp implements Vop {
   @Override public void eval(final State ctx) {
      ctx.push(ctx.nullPointer());
   }

   @Override public String toString() {
      return "ACONST_NULL";
   }
}
