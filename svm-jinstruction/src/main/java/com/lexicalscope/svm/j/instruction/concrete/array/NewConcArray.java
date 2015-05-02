package com.lexicalscope.svm.j.instruction.concrete.array;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.klass.SClass;

public final class NewConcArray implements ArrayConstructor {
   public void newConcreteArray(
         final JState ctx,
         final int arrayLength,
         final InitStrategy initStrategy) {
      final Object initValue = initStrategy.initialValue(ctx);

      final ObjectRef arrayAddress = ctx.newObject(new Allocatable() {
         @Override public int allocateSize() {
            return arrayLength + NewArrayOp.ARRAY_PREAMBLE;
         }
      });
      initArrayPreamble(ctx, arrayAddress, arrayLength);

      for (int i = 0; i < arrayLength; i++) {
         ctx.put(arrayAddress, NewArrayOp.ARRAY_PREAMBLE + i, initValue);
      }
      ctx.push(arrayAddress);
   }

   public static void initArrayPreamble(final JState ctx, final ObjectRef arrayAddress, final Object arrayLength) {
      // TODO - arrays can have different types
      final SClass objectArrayClass = ctx.loadKlassFor(internalName(Object[].class));
      ctx.put(arrayAddress, NewArrayOp.ARRAY_CLASS_OFFSET, objectArrayClass);
      ctx.put(arrayAddress, NewArrayOp.ARRAY_LENGTH_OFFSET, arrayLength);
   }

   @Override public void newArray(final JState ctx, final InitStrategy initStrategy) {
      newConcreteArray(ctx, (int) ctx.pop(), initStrategy);
   }
}