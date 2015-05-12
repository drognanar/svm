package com.lexicalscope.svm.j.instruction.factory;

import com.lexicalscope.svm.j.instruction.InstructionInternal;
import com.lexicalscope.svm.j.instruction.LinearOp;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.InstructionCode;
import com.lexicalscope.svm.vm.j.Vop;

public abstract class AbstractInstructionSink implements InstructionSource.InstructionSink {
   private int line;

   @Override public void line(final int line) {
      this.line = line;
   }

   @Override public final void nextOp(final Vop op, final InstructionCode code) {
      assert !(op instanceof Instruction);
      nextInstruction(new InstructionInternal(op, code, line));
   }

   @Override public final void linearOp(final Vop node, final InstructionCode code) {
      assert !(node instanceof LinearOp);
      nextOp(new LinearOp(node), code);
   }

   @Override public void noOp() { }

   protected abstract void nextInstruction(InstructionInternal node);
}
