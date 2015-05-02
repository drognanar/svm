package com.lexicalscope.svm.j.instruction.concrete.object;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.StaticsMarker;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class GetComponentClassOp implements Vop {
   @Override public void eval(final JState ctx) {
      final SClass klassFromHeap = ((StaticsMarker) ctx.get((ObjectRef) ctx.pop(), SClass.OBJECT_TYPE_MARKER_OFFSET)).klass();
      if(klassFromHeap.isArray()) {
         ctx.push(ctx.whereMyStaticsAt(klassFromHeap.componentType()));
      } else {
         ctx.push(ctx.nullPointer());
      }
   }

   @Override public String toString() {
      return "GET CLASS";
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.nativ3();
   }
}
