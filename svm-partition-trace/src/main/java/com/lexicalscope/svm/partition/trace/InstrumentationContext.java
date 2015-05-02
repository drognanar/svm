package com.lexicalscope.svm.partition.trace;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class InstrumentationContext {
   private final JState state;
   private final SMethodDescriptor methodName;

   public InstrumentationContext(final SMethodDescriptor methodName, final JState state) {
      this.methodName = methodName;
      this.state = state;
   }

   public StackFrame currentFrame() {
      return state.currentFrame();
   }

   public JState state() {
      return state;
   }

   public SClass receiverKlass() {
      final int argSize = methodName.argSize();
      final ObjectRef receiver = (ObjectRef) state.peek(argSize)[0];

      return (SClass) state.get(receiver, SClass.OBJECT_TYPE_MARKER_OFFSET);
   }
}
