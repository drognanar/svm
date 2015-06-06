package com.lexicalscope.svm.j.instruction.concrete.klass;

import static com.lexicalscope.svm.vm.j.klass.SClass.OBJECT_TYPE_MARKER_OFFSET;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class CheckCastOp implements Vop {
   private final KlassInternalName klassName;

   public CheckCastOp(final KlassInternalName klassName) {
      this.klassName = klassName;
   }

   @Override public void eval(final JState ctx) {
      final ObjectRef address = (ObjectRef) ctx.peek();
      if(ctx.nullPointer().equals(address)) {
         return;
      }

      final SClass classFromHeap = (SClass) ctx.get(address, OBJECT_TYPE_MARKER_OFFSET);
      final SClass classFromInstruction = ctx.loadKlassFor(klassName);

      if(classFromHeap == null || classFromHeap.instanceOf(classFromInstruction)) {
         return;
      }

      ctx.pop();

      // TODO[tim]: should throw an in-game class cast exception
      throw new UnsupportedOperationException();
   }

   @Override public String toString() {
      return "CHECKCAST " + klassName;
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.checkcast();
   }
}
