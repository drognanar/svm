package com.lexicalscope.svm.j.instruction.concrete.object;

import static com.lexicalscope.svm.j.instruction.concrete.object.ObjectTagMetaKey.OBJECT_TAG;
import static com.lexicalscope.svm.vm.j.klass.SClass.OBJECT_MARKER_OFFSET;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.klass.SClass;

public final class NewObjectOp implements Op<ObjectRef> {
   private final String klassDesc;

   public NewObjectOp(final String klassDesc) {
      this.klassDesc = klassDesc;
   }

   @Override public ObjectRef eval(final JState ctx) {
      // TODO[tim]: linking should remove this
      final SClass klass = ctx.load(klassDesc);
      final ObjectRef address = allocateObject(ctx, klass);
      ctx.push(address);

      return address;
   }

   public static ObjectRef allocateObject(final JState ctx, final SClass klass) {
      final ObjectRef address = ctx.newObject(klass, ctx.getFrameMeta(OBJECT_TAG));
      ctx.put(address, OBJECT_MARKER_OFFSET, klass);

      final Object nullPointer = ctx.nullPointer();
      int fieldOffset = OBJECT_MARKER_OFFSET + 1;
      for (final Object initialValue : klass.fieldInit()) {
         ctx.put(address, fieldOffset, initialValue == null ? nullPointer : initialValue);
         fieldOffset++;
      }
      return address;
   }

   @Override
   public String toString() {
      return String.format("NEW %s", klassDesc);
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.newobject(klassDesc);
   }
}