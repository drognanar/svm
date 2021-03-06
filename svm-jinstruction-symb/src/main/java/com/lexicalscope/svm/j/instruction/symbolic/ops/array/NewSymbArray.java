package com.lexicalscope.svm.j.instruction.symbolic.ops.array;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayConstructor;
import com.lexicalscope.svm.j.instruction.concrete.array.InitStrategy;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.j.instruction.concrete.array.NewConcArray;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayZeroedSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;
import com.lexicalscope.svm.z3.FeasibilityChecker.ISimplificationResult;

public class NewSymbArray implements ArrayConstructor {
   public static final int ARRAY_SYMBOL_OFFSET = NewArrayOp.ARRAY_PREAMBLE + 0;
   private final FeasibilityChecker feasibilityChecker;

   public NewSymbArray(final FeasibilityChecker feasibilityChecker) {
      this.feasibilityChecker = feasibilityChecker;
   }

   @Override public void newArray(final JState ctx, final InitStrategy initStrategy) {
      final Object top = ctx.pop();
      if(top instanceof ISymbol) {
         feasibilityChecker.simplifyBv32Expr((ISymbol) top, new ISimplificationResult(){
            @Override public void simplifiedToValue(final int arrayLength) {
               newConcreteArray(ctx, initStrategy, arrayLength);
            }

            @Override public void simplified(final ISymbol simplification) {
               newSymbolicArray(ctx, initStrategy, simplification);
            }});
      } else if (top instanceof Integer) {
         newConcreteArray(ctx, initStrategy, (int) top);
      } else {
         throw new UnsupportedOperationException("array length " + top);
      }
   }

   private void newSymbolicArray(final JState ctx, final InitStrategy initStrategy, final ISymbol arrayLengthSymbol) {
      newSymbolicArray(ctx, arrayLengthSymbol, new IArrayZeroedSymbol());
   }

   public static void newSymbolicArray(final JState ctx, final ISymbol arrayLength, final IArraySymbol arrayValueSymbol) {
      final ObjectRef arrayAddress = ctx.newObject(new Allocatable() {
         @Override public int allocateSize() {
            return NewArrayOp.ARRAY_PREAMBLE + 1;
         }
      });
      NewConcArray.initArrayPreamble(ctx, arrayAddress, arrayLength);
      // TODO[tim]: support other kinds of arrays
      ctx.put(arrayAddress, ARRAY_SYMBOL_OFFSET, arrayValueSymbol);
      ctx.push(arrayAddress);
   }

   private void newConcreteArray(final JState ctx, final InitStrategy initStrategy, final int arrayLength) {
      new NewConcArray().newConcreteArray(ctx, arrayLength, initStrategy);
   }
}
