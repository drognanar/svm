package com.lexicalscope.svm.j.instruction.concrete.object;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public final class GetClassOp implements Vop {
   @Override public void eval(final JState ctx) {
      final SClass klassFromHeap = klassFromHeap(ctx);
      final ObjectRef klassRef = ctx.whereMyStaticsAt(klassFromHeap);
      ctx.push(klassRef);
   }

   public SClass klassFromHeap(final JState ctx) {
      return klassFromHeap(ctx, (ObjectRef) ctx.pop());
   }

   public SClass klassFromHeap(final JState ctx, final ObjectRef objectRef) {
      return (SClass) ctx.get(objectRef, SClass.OBJECT_TYPE_MARKER_OFFSET);
   }

   @Override public String toString() {
      return "GET CLASS";
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.nativ3();
   }
}
