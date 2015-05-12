package com.lexicalscope.svm.j.instruction.symbolic;

import static com.lexicalscope.svm.j.instruction.symbolic.ops.array.NewSymbArray.newSymbolicArray;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class LoadSymbolicArrayArg implements Vop {
   private final IArrayAndLengthSymbols arg;

   public LoadSymbolicArrayArg(final IArrayAndLengthSymbols arg) {
      this.arg = arg;
   }

   @Override public void eval(final JState ctx) {
      newSymbolicArray(ctx, arg.getLengthSymbol(), arg.getArraySymbol());
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.loadarg(arg);
   }
}
