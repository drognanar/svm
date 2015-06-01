package com.lexicalscope.svm.search2;

import static com.lexicalscope.svm.partition.trace.TraceBuilder.terminateTrace;
import static com.lexicalscope.svm.search2.Side.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.symb.tree.TraceMetaExtractor;
import com.lexicalscope.svm.search.GuidedSearchObserver;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class TreeSearch implements StateSearch<JState> {
   private final List<JState> results = new ArrayList<>();

   private final TreeSearchStateSelection stateSelection;
   private final GuidedSearchObserver observer;
   private final FeasibilityChecker feasibilityChecker;
   private final TraceMetaExtractor metaExtractor;

   private final TraceTree tt;
   private final TraceTreeTracker tracker;
   private final TraceTreeSearchState searchState;

   private JState pending;

   private boolean pstateGiven;
   private boolean qstateGiven;

   public TreeSearch(
         final GuidedSearchObserver observer,
         final FeasibilityChecker feasibilityChecker,
         final TreeSearchStateSelection randomiser) {
      this.observer = observer;
      this.feasibilityChecker = feasibilityChecker;
      this.stateSelection = randomiser;
      this.tracker = new TraceTreeTracker();
      this.tt = new TraceTree(randomiser, tracker);
      this.searchState = new TraceTreeSearchState(tt);
      metaExtractor = new TraceMetaExtractor();
   }

   @Override public JState pendingState() {
      if(pending == null) {
         if(searchState.currentSide().equals(PSIDE) && tracker.anyQStatesAvailable()) {
            pickAQstate();
         } else if (searchState.currentSide().equals(QSIDE) && tracker.anyPStatesAvailable()) {
            pickAPstate();
         } else if (tracker.anyPStatesAvailable()) {
            pickAPstate();
         } else if (tracker.anyQStatesAvailable()) {
            pickAQstate();
         }

         if(pending != null) {
            observer.picked(pending, searchState.currentSide());
         }
      }
      return pending;
   }

   private void pickAQstate() {
      final TraceTree selectedTree = stateSelection.qnode(tracker.qstatesAvailable());
      pending = selectedTree.qStates().pickState();

      searchState.search(QSIDE, selectedTree);
   }

   private void pickAPstate() {
      final TraceTree selectedTree = stateSelection.pnode(tracker.pstatesAvailable());
      pending = selectedTree.pStates().pickState();

      searchState.search(PSIDE, selectedTree);
   }

   @Override public void reachedLeaf() {
      observer.leaf(pending);
      results.add(pending);

      final Trace goal = metaExtractor.goal(pending);
      pushGoalToCurrentNode(terminateTrace(goal));

      pending = null;
   }

   @Override public void fork(final JState parent, final JState[] states) {
      assert states.length == 2;
      observer.forkAt(parent);
      pushStateToSearchLater(searchState.currentNode(), states[0]);
      pushStateToSearchLater(searchState.currentNode(), states[1]);
      pending = null;
   }

   @Override public void forkDisjoined(final JState parent, final JState[] states) {
      observer.goal(pending);

      final Trace goal = metaExtractor.goal(pending);
      final TraceTree child = pushGoalToCurrentNode(goal);

      observer.forkAt(parent);
      for (JState state : states) {
         final Trace disjoinedGoal = metaExtractor.goal(state);
         final TraceTree disjoinedChild = child.child(disjoinedGoal);

         pushStateToSearchLater(disjoinedChild, state);
      }

      pending = null;
   }

   @Override public void goal() {
      observer.goal(pending);

      final Trace goal = metaExtractor.goal(pending);
      final TraceTree child = pushGoalToCurrentNode(goal);

      pushStateToSearchLater(child, pending);

      pending = null;
   }

   private TraceTree pushGoalToCurrentNode(final Trace goal) {
      final TraceTree child = searchState.currentNode().child(goal);

      switch (searchState.currentSide()) {
         case PSIDE:
            child.disjoinP(metaExtractor.pc(pending));
            break;

         case QSIDE:
            child.disjoinQ(metaExtractor.pc(pending));
            break;
      }
      new TraceTreeChildMismatchDetector(feasibilityChecker).mismatch(searchState.currentNode(), new MismatchReport(){
         @Override public void mismatch(final FeasibilityChecker checker, final Trace nodeTraceP, final BoolSymbol pPc, final Trace nodeTraceQ, final BoolSymbol qPc) {
            throw new PartitionViolationException(checker, nodeTraceP, pPc, nodeTraceQ, qPc);
         }});
      return child;
   }

   private void pushStateToSearchLater(final TraceTree node, final JState state) {
      switch (searchState.currentSide()) {
         case PSIDE:
            node.pState(state);
            break;

         case QSIDE:
            node.qState(state);
            break;
      }
   }

   @Override public JState firstResult() {
      if (results.size() == 0) {
         return null;
      }
      return results.get(0);
   }

   @Override public Collection<JState> results() {
      return results;
   }

   @Override public void consider(final JState state) {
      if(!pstateGiven && !qstateGiven) {
         metaExtractor.configureInitial(state);
         tt.pState(state);
         pstateGiven = true;
      } else if (!qstateGiven) {
         metaExtractor.configureInitial(state);
         tt.qState(state);
         qstateGiven = true;
      } else {
         throw new IllegalStateException("only one pstate and one qstate can be considered");
      }
   }
}
