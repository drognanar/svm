package com.lexicalscope.svm.j.instruction.symbolic.ops;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.symbolic.ops.object.SymbolicObject;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.OTerminalSymbol;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class LoadSymbolicObjectArg implements Vop {
   private final OTerminalSymbol symbol;

   public LoadSymbolicObjectArg(final OTerminalSymbol symbol) {
      this.symbol = symbol;
   }

   @Override public void eval(final JState ctx) {
      final ObjectRef newObject = ctx.newObject(new Allocatable() {
         @Override public int allocateSize() {
            return 2;
         }
      });
      ctx.put(newObject, SClass.OBJECT_TYPE_MARKER_OFFSET, new SymbolicObject(symbol));
      ctx.push(newObject);
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.loadarg(symbol);
   }
}
