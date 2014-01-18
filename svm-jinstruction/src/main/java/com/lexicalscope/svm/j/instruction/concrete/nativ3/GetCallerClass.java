package com.lexicalscope.svm.j.instruction.concrete.nativ3;

import com.lexicalscope.symb.klass.SMethodDescriptor;
import com.lexicalscope.symb.stack.StackFrame;
import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.Vop;

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
}