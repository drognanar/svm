package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.search.GuidedSearchObserver;
import com.lexicalscope.svm.search.RandomSeedPseudoRandomiser;
import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.conc.StateSearchFactory;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class TreeSearchFactory implements StateSearchFactory {
   private final FeasibilityChecker feasibilityChecker;
   private final GuidedSearchObserver observer;
   private final Randomiser randomiser;

   public TreeSearchFactory(
         final GuidedSearchObserver observer,
         final FeasibilityChecker feasibilityChecker,
         final Randomiser randomiser) {
      this.feasibilityChecker = feasibilityChecker;
      this.observer = observer;
      this.randomiser = randomiser;
   }

   public TreeSearchFactory(
         final GuidedSearchObserver observer,
         final FeasibilityChecker feasibilityChecker) {
      this(observer, feasibilityChecker, new RandomSeedPseudoRandomiser());
   }

   @Override public StateSearch<JState> search() {
      return new TreeSearch(
                  observer,
                  feasibilityChecker,
                  randomiser);
   }
}
