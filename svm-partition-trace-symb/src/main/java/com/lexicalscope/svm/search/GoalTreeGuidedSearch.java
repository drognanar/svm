package com.lexicalscope.svm.search;

import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePair;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.j.JState;

@Deprecated() // please use TreeSearch instead
public class GoalTreeGuidedSearch implements StateSearch<JState> {
   private final GoalTreeCorrespondence correspondence;

   private final SearchMetaExtractor goalExtractor;
   private final List<JState> result = new ArrayList<>();
   private GoalTreePair correspondenceUnderConsideration;
   private boolean pInitialised;
   private boolean qInitialised;
   private JState pending;

   private GuidedSearchState side;

   private final GuidedSearchObserver observer;

   public GoalTreeGuidedSearch(
         final GuidedSearchObserver observer,
         final GoalTreeCorrespondence correspondence,
         final SearchMetaExtractor goalExtractor,
         final Randomiser randomiser) {
      this.observer = observer;
      this.correspondence = correspondence;
      this.goalExtractor = goalExtractor;
      side = new GuidedSearchInitialState(randomiser);
   }

   @Override public JState pendingState() {
      if(pending == null) {
         return switchSides();
      }
      return pending;
   }

   private JState switchSides() {
      side.searchedSide(correspondence, correspondenceUnderConsideration);

      while(side.searchMore(correspondence)) {
         side = side.nextSide();
         correspondenceUnderConsideration =
               side.pickCorrespondence(correspondence, correspondenceUnderConsideration);

         if(side.isOpen(correspondenceUnderConsideration)) {
            pending = side.pickSearchNode(correspondenceUnderConsideration);
            observer.picked(pending, side);
            return pending;
         }
      }
      return pending = null;
   }

   @Override public void reachedLeaf() {
      observer.leaf(pending);
      result.add(pending);
      switchSides();
   }

   @Override public void fork(final JState parent, final JState[] states) {
//      assert states.length == 2;
      observer.forkAt(parent);
      side.fork(correspondenceUnderConsideration, states);
      switchSides();
   }

   @Override public void goal() {
      observer.goal(pending);
      side.goal(
            correspondence,
            correspondenceUnderConsideration,
            pending.getMeta(TRACE),
            pending,
            goalExtractor.pc(pending));

      switchSides();
   }

   @Override public JState firstResult() {
      return result.get(0);
   }

   @Override public Collection<JState> results() {
      return result;
   }

   @Override public void consider(final JState state) {
      if(!pInitialised) {
         goalExtractor.configureInitial(state);
         correspondence.pInitial(state);
         pInitialised = true;
      } else if(!qInitialised) {
         goalExtractor.configureInitial(state);
         correspondence.qInitial(state);
         qInitialised = true;
      } else {
         throw new IllegalStateException("only 2 initial states can be considered, not " + state);
      }
   }
}
