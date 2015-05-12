package com.lexicalscope.svm.j.instruction.symbolic.ops;

import static com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp.ARRAY_LENGTH_OFFSET;
import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.asISymbol;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayLoadOp;
import com.lexicalscope.svm.j.instruction.symbolic.ops.array.NewSymbArray;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySelectSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SArrayLoadOp implements Vop {
   private final FeasibilityChecker feasibilityChecker;
   private final ArrayLoadOp concreteArrayLoad;

   public SArrayLoadOp(final FeasibilityChecker feasibilityChecker, final ArrayLoadOp concreteArrayLoad) {
      this.feasibilityChecker = feasibilityChecker;
      this.concreteArrayLoad = concreteArrayLoad;
   }

   @Override public void eval(final JState ctx) {
      final Object offset = ctx.pop();
      final ObjectRef arrayref = (ObjectRef) ctx.pop();

      final Object arrayLength = ctx.get(arrayref, ARRAY_LENGTH_OFFSET);
      if(arrayLength instanceof Integer && offset instanceof Integer) {
         ctx.push(concreteArrayLoad.loadFromHeap(ctx, arrayref, (int) offset));
      } else if (arrayLength instanceof ISymbol) {
         ctx.push(loadFromSymbolicArray(ctx, arrayref, offset));
      } else {
         throw new UnsupportedOperationException("concrete array symbolic offset " + offset);
      }
   }

   private Object loadFromSymbolicArray(final JState ctx, final ObjectRef arrayref, final Object offset) {
      return new IArraySelectSymbol(
            (IArraySymbol) ctx.get(arrayref, NewSymbArray.ARRAY_SYMBOL_OFFSET),
            asISymbol(offset));
   }

   @Override public String toString() {
      return "ARRAYLOAD";
   }

   public static Vop iaLoad(final FeasibilityChecker feasibilityChecker) {
      return new SArrayLoadOp(feasibilityChecker, ArrayLoadOp.iaLoad());
   }

   public static Vop aaLoad(final FeasibilityChecker feasibilityChecker) {
      return new SArrayLoadOp(feasibilityChecker, ArrayLoadOp.aaLoad());
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.arrayload();
   }
}
