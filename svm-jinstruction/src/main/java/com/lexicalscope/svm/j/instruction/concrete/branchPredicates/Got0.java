package com.lexicalscope.svm.j.instruction.concrete.branchPredicates;

import com.lexicalscope.svm.j.instruction.concrete.branch.BranchPredicate;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;

public class Got0 implements BranchPredicate {
   @Override public Boolean eval(final JState ctx) {
      return true;
   }

   @Override public String toString() {
      return "GOTO";
   }

   @Override public <S> S query(final InstructionQuery<S> instructionQuery) {
      return instructionQuery.got0();
   }
}
