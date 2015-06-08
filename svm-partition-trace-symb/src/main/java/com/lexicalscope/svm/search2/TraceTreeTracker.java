package com.lexicalscope.svm.search2;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class TraceTreeTracker implements TraceTreeObserver {
   private final FastRemovalList<TraceTree> pstatesAvailable = new FastRemovalList<>();
   private final FastRemovalList<TraceTree> qstatesAvailable = new FastRemovalList<>();

   @Override public FastListLocation pstateAvailable(final TraceTree traceTree) {
      return pstatesAvailable.addItem(traceTree);
   }

   @Override public void pstateUnavailable(final TraceTree traceTree) {
      pstatesAvailable.remove(traceTree.pLocation());
   }

   @Override public FastListLocation qstateAvailable(final TraceTree traceTree) {
      return qstatesAvailable.addItem(traceTree);
   }

   @Override public void qstateUnavailable(final TraceTree traceTree) {
      qstatesAvailable.remove(traceTree.qLocation());
   }

   public FastRemovalList<TraceTree> pstatesAvailable() {
      return pstatesAvailable;
   }

   public FastRemovalList<TraceTree> qstatesAvailable() {
      return qstatesAvailable;
   }

   public static FeatureMatcher<TraceTreeTracker, FastRemovalList<TraceTree>> hasPstatesAvailable(final Matcher<? super FastRemovalList<TraceTree>> contains) {
      return new FeatureMatcher<TraceTreeTracker, FastRemovalList<TraceTree>>(contains, "tree with p states available", "pStates available") {
         @Override protected FastRemovalList<TraceTree> featureValueOf(final TraceTreeTracker actual) {
            return actual.pstatesAvailable();
         }
      };
   }

   public boolean anyPStatesAvailable() {
      return !pstatesAvailable.isEmpty();
   }

   public boolean anyQStatesAvailable() {
      return !qstatesAvailable.isEmpty();
   }
}