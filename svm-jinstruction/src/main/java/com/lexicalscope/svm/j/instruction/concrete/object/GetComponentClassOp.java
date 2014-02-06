package com.lexicalscope.svm.j.instruction.concrete.object;

import com.lexicalscope.symb.vm.j.InstructionQuery;
import com.lexicalscope.symb.vm.j.State;
import com.lexicalscope.symb.vm.j.StaticsMarker;
import com.lexicalscope.symb.vm.j.Vop;
import com.lexicalscope.symb.vm.j.j.klass.SClass;

public class GetComponentClassOp implements Vop {
   @Override public void eval(final State ctx) {
      final SClass klassFromHeap = ((StaticsMarker) ctx.get(ctx.pop(), SClass.OBJECT_MARKER_OFFSET)).klass();
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
