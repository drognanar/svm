package com.lexicalscope.svm.search;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePair;
import com.lexicalscope.svm.vm.j.JState;

class GuidedSearchSearchingQ implements GuidedSearchState {
   private final GuidedSearchSearchingP searchingP;
   private final Randomiser randomiser;

   public GuidedSearchSearchingQ(final GuidedSearchSearchingP searchingP, final Randomiser randomiser) {
      this.searchingP = searchingP;
      this.randomiser = randomiser;
   }

   @Override public void searchedSide(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair pair) {
      if(pair.isOpen()) {
         correspondence.stillOpen(pair);
      }
   }

   @Override public boolean searchMore(final GoalTreeCorrespondence correspondence) {
      return correspondence.hasOpenChildren();
   }

   @Override public GuidedSearchState nextSide() {
      return searchingP;
   }

   @Override public boolean isOpen(final GoalTreePair correspondenceUnderConsideration) {
      return correspondenceUnderConsideration.qsideIsOpen();
   }

   @Override public JState pickSearchNode(final GoalTreePair correspondenceUnderConsideration) {
      return correspondenceUnderConsideration.openQNode(randomiser);
   }

   @Override public GoalTreePair pickCorrespondence(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair correspondenceUnderConsideration) {
      return correspondenceUnderConsideration;
   }

   @Override public void fork(
         final GoalTreePair correspondenceUnderConsideration, final JState[] states) {
      correspondenceUnderConsideration.expandQ(states);
   }

   @Override public void goal(
         final GoalTreeCorrespondence correspondence,
         final GoalTreePair parent,
         final Trace goal,
         final JState state,
         final BoolSymbol pc) {
      correspondence.reachedQ(parent, goal, state, pc);
   }

   @Override public String toString() {
      return "Searching Q side";
   }
}