package com.lexicalscope.svm.search2;

import java.util.Collection;

import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.vm.StateSearch;

public class GoalTreeGuidedSearchStrategy<T, S> implements StateSearch<S> {
   private final GoalTreeCorrespondence<T, S> correspondence;
   private boolean qNext;

   public GoalTreeGuidedSearchStrategy(final GoalTreeCorrespondence<T, S> correspondence) {
      this.correspondence = correspondence;
   }

   @Override public S pendingState() {
      return null;
   }

   @Override public void reachedLeaf() {
      // TODO Auto-generated method stub

   }

   @Override public void fork(final S[] states) {
      // TODO Auto-generated method stub

   }

   @Override public void goal() {
      // TODO Auto-generated method stub

   }

   @Override public S firstResult() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override public Collection<S> results() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override public void consider(final S state) {

   }
}