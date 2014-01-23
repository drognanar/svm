package com.lexicalscope.svm.j.instruction.concrete.branch;

import com.lexicalscope.symb.vm.j.State;
import com.lexicalscope.symb.vm.j.Vop;

public final class BranchInstruction implements Vop {
   private final BranchPredicate branchPredicate;

   public BranchInstruction(final BranchPredicate branchPredicate) {
      this.branchPredicate = branchPredicate;
   }

   @Override
   public String toString() {
      return branchPredicate.toString();
   }

   @Override public void eval(final State ctx) {
      ctx.advanceTo(branchPredicate.eval(ctx) ? ctx.instructionJmpTarget() : ctx.instructionNext());
   }
}