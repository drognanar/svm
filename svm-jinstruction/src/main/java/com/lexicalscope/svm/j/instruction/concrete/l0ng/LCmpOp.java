package com.lexicalscope.svm.j.instruction.concrete.l0ng;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class LCmpOp implements Vop {
   @Override public void eval(final JState ctx) {
      final long value2 = (long) ctx.popDoubleWord();
      final long value1 = (long) ctx.popDoubleWord();

      Object result;
      if(value1 > value2) {
         result = 1;
      } else if(value1 < value2) {
         result = -1;
      } else {
         result = 0;
      }
      ctx.push(result);
   }

   @Override public String toString() {
      return "LCMP";
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.lcmp();
   }
}
