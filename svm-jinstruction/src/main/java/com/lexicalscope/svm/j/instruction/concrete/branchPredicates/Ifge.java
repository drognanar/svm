package com.lexicalscope.svm.j.instruction.concrete.branchPredicates;

import com.lexicalscope.svm.j.instruction.concrete.branch.BranchPredicate;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;

public class Ifge implements BranchPredicate {
   @Override public Boolean eval(final JState ctx) {
      return (int) ctx.pop() >= 0;
   }

   @Override public String toString() {
      return "GE";
   }

   @Override public <S> S query(final InstructionQuery<S> instructionQuery) {
      return instructionQuery.ifge();
   }
}
