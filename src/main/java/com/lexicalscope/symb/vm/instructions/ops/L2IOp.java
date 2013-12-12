package com.lexicalscope.symb.vm.instructions.ops;

import com.lexicalscope.symb.vm.Heap;
import com.lexicalscope.symb.vm.Stack;
import com.lexicalscope.symb.vm.StackFrame;
import com.lexicalscope.symb.vm.Statics;
import com.lexicalscope.symb.vm.Vop;

public class L2IOp implements Vop {
   @Override public void eval(final StackFrame stackFrame, final Stack stack, final Heap heap, final Statics statics) {
      stackFrame.push((int)(long)stackFrame.pop());
   }
}
