package com.lexicalscope.svm.j.instruction.factory;

import com.lexicalscope.symb.vm.j.InstructionQuery;
import com.lexicalscope.symb.vm.j.Op;
import com.lexicalscope.symb.vm.j.State;
import com.lexicalscope.symb.vm.j.Vop;


public class VopAdapter implements Vop {
   private final Op<?> op;

   public VopAdapter(final Op<?> op) {
      this.op = op;
   }

   @Override public void eval(final State ctx) {
      op.eval(ctx);
   }

   @Override public String toString() {
      return op.toString();
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return op.query(instructionQuery);
   }
}
