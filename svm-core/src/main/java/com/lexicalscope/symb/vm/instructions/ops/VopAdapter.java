package com.lexicalscope.symb.vm.instructions.ops;

import com.lexicalscope.symb.vm.Context;
import com.lexicalscope.symb.vm.Op;
import com.lexicalscope.symb.vm.Vop;

public class VopAdapter implements Vop {
   private final Op<?> op;

   public VopAdapter(final Op<?> op) {
      this.op = op;
   }

   @Override public void eval(final Context ctx) {
      op.eval(ctx);
   }

   @Override public String toString() {
      return op.toString();
   }
}
