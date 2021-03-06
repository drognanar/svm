package com.lexicalscope.svm.j.instruction.concrete.ops;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class UnaryOp implements Vop {
   private final UnaryOperator operator;

   public UnaryOp(final UnaryOperator operator) {
      this.operator = operator;
   }

   @Override public void eval(final JState ctx) {
      ctx.push(operator.eval(ctx.pop()));
   }

   @Override
   public String toString() {
      return operator.toString();
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.unaryop();
   }
}
