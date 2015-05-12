package com.lexicalscope.svm.search;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePair;
import com.lexicalscope.svm.vm.j.JState;

class GuidedSearchSearchingP implements GuidedSearchState {
   private final GuidedSearchState searchingQy;
   private final Randomiser randomiser;

   public GuidedSearchSearchingP(final Randomiser randomiser) {
      this.randomiser = randomiser;
      searchingQy = new GuidedSearchSearchingQ(this, randomiser);
   }

   @Override public void searchedSide(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair pair) {
   }

   @Override public GoalTreePair pickCorrespondence(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair correspondenceUnderConsideration) {
      return correspondence.randomOpenChild(randomiser);
   }

   @Override public boolean searchMore(final GoalTreeCorrespondence correspondence) {
      return true;
   }

   @Override public GuidedSearchState nextSide() {
      return searchingQy;
   }

   @Override public boolean isOpen(final GoalTreePair correspondenceUnderConsideration) {
      return correspondenceUnderConsideration.psideIsOpen();
   }

   @Override public JState pickSearchNode(final GoalTreePair correspondenceUnderConsideration) {
      return correspondenceUnderConsideration.openPNode(randomiser);
   }

   @Override public void fork(
         final GoalTreePair correspondenceUnderConsideration, final JState[] states) {
      correspondenceUnderConsideration.expandP(states);
   }

   @Override public void goal(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair parent,
         final Trace goal,
         final JState state,
         final BoolSymbol pc) {
      correspondence.reachedP(parent, goal, state, pc);
   }

   @Override public String toString() {
      return "Searching P side";
   }
}