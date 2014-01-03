package com.lexicalscope.symb.vm.instructions;

import com.lexicalscope.symb.vm.Heap;
import com.lexicalscope.symb.vm.Stack;
import com.lexicalscope.symb.vm.StackFrame;
import com.lexicalscope.symb.vm.Statics;
import com.lexicalscope.symb.vm.Vop;

public class GetCallerClass implements Vop {
   @Override public void eval(final StackFrame stackFrame, final Stack stack, final Heap heap, final Statics statics) {
      // TODO[tim]: demeter
      stackFrame.push(statics.whereMyClassAt(stack.caller().method().klass()));
   }
}
