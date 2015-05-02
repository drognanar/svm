package com.lexicalscope.svm.j.instruction.concrete.klass;

import static com.lexicalscope.svm.j.instruction.concrete.klass.DefinePrimitiveClassesOp.primitivesContains;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public final class GetPrimitiveClass implements Vop {
   @Override public void eval(final JState ctx) {
      final ObjectRef primitiveNameRef = (ObjectRef) ctx.pop();
      final KlassInternalName klassName = internalName(inGameStringToRealLifeString(ctx, primitiveNameRef));
      assert primitivesContains(klassName) : klassName + " is not a primitive";
      ctx.push(ctx.whereMyClassAt(klassName));
   }

   private String inGameStringToRealLifeString(final JState ctx, final ObjectRef primitiveNameRef) {
      final SClass string = ctx.loadKlassFor(JavaConstants.STRING_CLASS);
      final int valueFieldIndex = string.fieldIndex(JavaConstants.STRING_VALUE_FIELD);
      return new String(extractCharArray(ctx, primitiveNameRef, valueFieldIndex)).intern();
   }

   private char[] extractCharArray(final JState ctx, final ObjectRef primitiveNameRef, final int valueFieldIndex) {
      final ObjectRef arrayPointer = (ObjectRef) ctx.get(primitiveNameRef, valueFieldIndex);
      return toCharArray(extractArray(ctx, arrayPointer));
   }

   private Object[] extractArray(final JState ctx, final ObjectRef arrayPointer) {
      final int arrayLength = (int) ctx.get(arrayPointer, NewArrayOp.ARRAY_LENGTH_OFFSET);
      final Object[] result = new Object[arrayLength];
      for (int i = 0; i < arrayLength; i++) {
         result[i] = ctx.get(arrayPointer, NewArrayOp.ARRAY_PREAMBLE + i);
      }
      return result;
   }

   private char[] toCharArray(final Object[] array) {
      final char[] result = new char[array.length];
      for (int i = 0; i < result.length; i++) {
         result[i] = (char) array[i];
      }
      return result;
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.nativ3();
   }
}
