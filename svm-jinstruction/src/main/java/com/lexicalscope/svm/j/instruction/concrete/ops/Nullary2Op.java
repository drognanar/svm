package com.lexicalscope.svm.j.instruction.concrete.ops;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class Nullary2Op implements Vop {
   private final Nullary2Operator operator;

   public Nullary2Op(final Nullary2Operator operator) {
      this.operator = operator;
   }

   @Override
   public String toString() {
      return operator.toString();
   }

   @Override public void eval(final JState ctx) {
      ctx.pushDoubleWord(operator.eval());
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.nularyop();
   }
}
