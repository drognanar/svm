package com.lexicalscope.svm.j.instruction.concrete.stack;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class Load2 implements Vop {
   private final int var;

   public Load2(final int var) {
      this.var = var;
   }

   @Override
   public String toString() {
      return String.format("LOAD2 %d", var);
   }

   @Override public void eval(final JState ctx) {
      ctx.pushDoubleWord(ctx.local(var));
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.load(var);
   }
}
