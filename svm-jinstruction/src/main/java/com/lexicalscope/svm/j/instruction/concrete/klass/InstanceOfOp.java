package com.lexicalscope.svm.j.instruction.concrete.klass;

import static com.lexicalscope.svm.vm.j.klass.SClass.OBJECT_TYPE_MARKER_OFFSET;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class InstanceOfOp implements Vop {
   private final KlassInternalName klassName;

   public InstanceOfOp(final KlassInternalName klassName) {
      this.klassName = klassName;
   }

   @Override public void eval(final JState ctx) {
      final ObjectRef address = (ObjectRef) ctx.pop();
      if(!ctx.nullPointer().equals(address)) {
         ctx.push(1);
         return;
      }

      final SClass classFromHeap = (SClass) ctx.get(address, OBJECT_TYPE_MARKER_OFFSET);
      final SClass classFromInstruction = ctx.loadKlassFor(klassName);

      if(classFromHeap.instanceOf(classFromInstruction)) {
         ctx.push(1);
         return;
      }
      ctx.push(0);
   }

   @Override public String toString() {
      return "INSTANCEOF";
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.instance0f();
   }
}
