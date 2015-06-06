package com.lexicalscope.svm.j.instruction.symbolic.ops;

import static com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp.ARRAY_LENGTH_OFFSET;
import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.asISymbol;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayStoreOp;
import com.lexicalscope.svm.j.instruction.symbolic.ops.array.NewSymbArray;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayStoreSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SArrayStoreOp implements Vop {
   private final FeasibilityChecker feasibilityChecker;
   private final ArrayStoreOp concreteArrayStore;

   private static ArrayStoreOp.ValueTransform charTransform = new ArrayStoreOp.ValueTransform() {
      @Override public Object transformForStore(final Object value) {
         if (value instanceof ISymbol) {
            return value;
         }
         return (char)(int)value;
      }
   };

   public SArrayStoreOp(final FeasibilityChecker feasibilityChecker, final ArrayStoreOp concreteArrayStore) {
      this.feasibilityChecker = feasibilityChecker;
      this.concreteArrayStore = concreteArrayStore;
   }

   @Override public void eval(final JState ctx) {
      final Object value = ctx.pop();
      final Object offset = ctx.pop();
      final ObjectRef arrayref = (ObjectRef) ctx.pop();

      final Object arrayLength = ctx.get(arrayref, ARRAY_LENGTH_OFFSET);
      if(arrayLength instanceof Integer && offset instanceof Integer) {
         concreteArrayStore.storeInHeap(ctx, arrayref, (int) offset, value);
      } else if (arrayLength instanceof ISymbol) {
         storeInSymbolicArray(ctx, arrayref, offset, value);
      } else {
         throw new UnsupportedOperationException("concrete array symbolic offset");
      }
   }

   private void storeInSymbolicArray(final JState ctx, final ObjectRef arrayref, final Object offset, final Object value) {
      final IArraySymbol symbol = (IArraySymbol) ctx.get(arrayref, NewSymbArray.ARRAY_SYMBOL_OFFSET);
      ctx.put(arrayref,
            NewSymbArray.ARRAY_SYMBOL_OFFSET,
            new IArrayStoreSymbol(symbol, asISymbol(offset), asISymbol(value)));
   }

   @Override public String toString() {
      return "ARRAYSTORE";
   }

   public static Vop aaStore(final FeasibilityChecker feasibilityChecker) {
      return new SArrayStoreOp(feasibilityChecker, ArrayStoreOp.aaStore());
   }

   public static Vop iaStore(final FeasibilityChecker feasibilityChecker) {
      return new SArrayStoreOp(feasibilityChecker, ArrayStoreOp.iaStore());
   }

   public static Vop caStore(final FeasibilityChecker feasibilityChecker) {
      return new SArrayStoreOp(feasibilityChecker, new ArrayStoreOp(charTransform));
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.arraystore();
   }
}
