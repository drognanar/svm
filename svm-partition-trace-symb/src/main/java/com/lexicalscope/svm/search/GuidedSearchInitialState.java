package com.lexicalscope.svm.search;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePair;
import com.lexicalscope.svm.vm.j.JState;

final class GuidedSearchInitialState implements GuidedSearchState {
   private final Randomiser randomiser;

   public GuidedSearchInitialState(final Randomiser randomiser) {
      this.randomiser = randomiser;
   }

   @Override public void searchedSide(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair pair) {
      // nothing
   }

   @Override public boolean searchMore(final GoalTreeCorrespondence correspondence) {
      return true;
   }

   @Override public GuidedSearchState nextSide() {
      return new GuidedSearchSearchingP(randomiser);
   }

   @Override public boolean isOpen(final GoalTreePair correspondence) {
      throw new UnsupportedOperationException();
   }

   @Override public JState pickSearchNode(final GoalTreePair correspondenceUnderConsideration) {
      throw new UnsupportedOperationException();
   }

   @Override public GoalTreePair pickCorrespondence(final GoalTreeCorrespondence correspondence, final GoalTreePair correspondenceUnderConsideration) {
      throw new UnsupportedOperationException();
   }

   @Override public void fork(final GoalTreePair correspondenceUnderConsideration, final JState[] states) {
      throw new UnsupportedOperationException();
   }

   @Override public void goal(final GoalTreeCorrespondence correspondence, final GoalTreePair correspondenceUnderConsideration, final Trace goal, final JState pending, final BoolSymbol pc) {
      throw new UnsupportedOperationException();
   }
}