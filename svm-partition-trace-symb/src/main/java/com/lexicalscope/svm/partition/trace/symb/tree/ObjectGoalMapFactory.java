package com.lexicalscope.svm.partition.trace.symb.tree;


public class ObjectGoalMapFactory implements GoalMapFactory {
   @Override public <N> GoalMap<N> newGoalMap() {
      return new ObjectGoalMap<N>();
   }
}
