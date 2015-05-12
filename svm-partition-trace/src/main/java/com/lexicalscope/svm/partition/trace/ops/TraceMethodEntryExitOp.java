package com.lexicalscope.svm.partition.trace.ops;

import static com.lexicalscope.svm.j.instruction.concrete.object.PartitionTagMetaKey.PARTITION_TAG;
import static com.lexicalscope.svm.partition.trace.HashTrace.CallReturn.RETURN;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;

import java.util.Objects;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.partition.trace.HashTrace.CallReturn;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

class TraceMethodEntryExitOp implements Vop {
   private final CallReturn callReturn;
   private final SMethodDescriptor methodName;

   public TraceMethodEntryExitOp(
         final SMethodDescriptor methodName,
         final CallReturn callReturn) {
      this.methodName = methodName;
      this.callReturn = callReturn;
   }

   @Override public void eval(final JState ctx) {
      final StackFrame previousFrame = ctx.previousFrame();
      final StackFrame currentFrame = ctx.currentFrame();

      if(callReturn.equals(CallReturn.CALL) && currentFrame.isDynamic()) {
         final ObjectRef receiver = (ObjectRef) currentFrame.local(0);
         assert receiver != null : "null receiver at call of " + methodName + " in frame " + currentFrame;
         currentFrame.setMeta(PARTITION_TAG, ctx.get(receiver, SClass.OBJECT_TAG_OFFSET));
      }

      final Object previousFrameTag = previousFrame.getMeta(PARTITION_TAG);
      final Object currentFrameTag = currentFrame.getMeta(PARTITION_TAG);

      if(previousFrameTag != null && currentFrameTag != null && !Objects.equals(previousFrameTag, currentFrameTag)) {
         final Object[] args;
         if(callReturn.equals(RETURN)) {
            args = ctx.peek(methodName.returnCount());
         } else {
            args = ctx.locals(methodName.argSize());
         }
         final Trace trace = ctx.getMeta(TRACE);
         ctx.setMeta(TRACE, trace.extend(methodName, callReturn, args));
         ctx.goal();
      }
   }

   @Override public String toString() {
      return "TRACE method " + callReturn;
   }

   @Override public <S> S query(final InstructionQuery<S> instructionQuery) {
      return instructionQuery.synthetic();
   }
}
