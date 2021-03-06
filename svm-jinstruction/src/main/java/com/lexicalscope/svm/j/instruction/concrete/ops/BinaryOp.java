package com.lexicalscope.svm.j.instruction.concrete.ops;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class BinaryOp implements Vop {
   private final BinaryOperator operator;

   public BinaryOp(final BinaryOperator operator) {
      this.operator = operator;
   }

   @Override public void eval(final JState ctx) {
      final Object right = ctx.pop();
      final Object left = ctx.pop();

      ctx.push(operator.eval(left, right));
   }

   @Override
   public String toString() {
      return operator.toString();
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.binaryop();
   }
}
