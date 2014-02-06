package com.lexicalscope.svm.j.instruction.concrete.nativ3;

import com.lexicalscope.symb.stack.StackFrame;
import com.lexicalscope.symb.vm.j.InstructionQuery;
import com.lexicalscope.symb.vm.j.State;
import com.lexicalscope.symb.vm.j.Vop;
import com.lexicalscope.symb.vm.j.j.klass.SMethodDescriptor;

public class GetCallerClass implements Vop {
   @Override public void eval(final State ctx) {
      // TODO[tim]: demeter
      final StackFrame callingFrame = ctx.previousFrame();
      final SMethodDescriptor callingFrameContext = (SMethodDescriptor) callingFrame.context();
      ctx.push(ctx.whereMyClassAt(callingFrameContext.klassName()));
   }

   @Override public String toString() {
      return "CallerClass";
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.nativ3();
   }
}
