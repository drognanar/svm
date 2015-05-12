package com.lexicalscope.svm.partition.trace.symb.tree;

import com.lexicalscope.svm.z3.FeasibilityChecker;

public class TraceGoalMapFactory implements GoalMapFactory {
    private final FeasibilityChecker checker;

   public TraceGoalMapFactory(final FeasibilityChecker checker) {
      this.checker = checker;
   }

   @Override public <N> GoalMap<N> newGoalMap() {
      return new TraceGoalMap<>(checker);
   }
}
