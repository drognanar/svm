package com.lexicalscope.symb.vm.instructions.ops.array;

import static org.objectweb.asm.Type.getInternalName;

import com.lexicalscope.symb.heap.Allocatable;
import com.lexicalscope.symb.vm.Context;
import com.lexicalscope.symb.vm.classloader.SClass;

public final class NewConcArray implements ArrayConstructor {
   public void newConcreteArray(
         final Context ctx,
         final int arrayLength,
         final InitStrategy initStrategy) {
      final Object initValue = initStrategy.initialValue(ctx);

      final Object arrayAddress = ctx.newObject(new Allocatable() {
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

   public static void initArrayPreamble(final Context ctx, final Object arrayAddress, final Object arrayLength) {
      // TODO - arrays can have different types
      final SClass objectArrayClass = ctx.load(getInternalName(Object[].class));
      ctx.put(arrayAddress, NewArrayOp.ARRAY_CLASS_OFFSET, objectArrayClass);
      ctx.put(arrayAddress, NewArrayOp.ARRAY_LENGTH_OFFSET, arrayLength);
   }

   @Override public void newArray(final Context ctx, final InitStrategy initStrategy) {
      newConcreteArray(ctx, (int) ctx.pop(), initStrategy);
   }
}