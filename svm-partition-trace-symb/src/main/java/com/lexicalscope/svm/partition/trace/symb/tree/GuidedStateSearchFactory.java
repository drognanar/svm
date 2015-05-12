package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondenceImpl.root;

import com.lexicalscope.svm.search.GoalTreeGuidedSearch;
import com.lexicalscope.svm.search.GuidedSearchObserver;
import com.lexicalscope.svm.search.RandomSeedPseudoRandomiser;
import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.conc.StateSearchFactory;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;

@Deprecated
public class GuidedStateSearchFactory implements StateSearchFactory {
   private final FeasibilityChecker feasibilityChecker;
   private final GuidedSearchObserver observer;
   private final Randomiser randomiser;

   public GuidedStateSearchFactory(
         final GuidedSearchObserver observer,
         final FeasibilityChecker feasibilityChecker,
         final Randomiser randomiser) {
      this.feasibilityChecker = feasibilityChecker;
      this.observer = observer;
      this.randomiser = randomiser;
   }

   public GuidedStateSearchFactory(
         final GuidedSearchObserver observer,
         final FeasibilityChecker feasibilityChecker) {
      this(observer, feasibilityChecker, new RandomSeedPseudoRandomiser());
   }

   @Override public StateSearch<JState> search() {
      return new GoalTreeGuidedSearch(
                  observer,
                  root(trace().build(), feasibilityChecker, new TraceGoalMapFactory(feasibilityChecker)),
                  new TraceMetaExtractor(),
                  randomiser);
   }
}
