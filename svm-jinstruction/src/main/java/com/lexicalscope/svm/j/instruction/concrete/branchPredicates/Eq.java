package com.lexicalscope.svm.j.instruction.concrete.branchPredicates;

import com.lexicalscope.svm.j.instruction.concrete.branch.BranchPredicate;
import com.lexicalscope.symb.vm.State;

public class Eq implements BranchPredicate {
	@Override public Boolean eval(final State ctx) {
	   return (int) ctx.pop() == 0;
	}

	@Override public String toString() {
	   return "EQ";
	}
}