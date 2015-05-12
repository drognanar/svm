package com.lexicalscope.svm.search2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class TraceTreeTracker implements TraceTreeObserver {
   private final List<TraceTree> pstatesAvailable = new ArrayList<TraceTree>();
   private final List<TraceTree> qstatesAvailable = new ArrayList<TraceTree>();

   @Override public void pstateAvailable(final TraceTree traceTree) {
      pstatesAvailable.add(traceTree);
   }

   @Override public void pstateUnavailable(final TraceTree traceTree) {
      pstatesAvailable.remove(traceTree);
   }

   @Override public void qstateAvailable(final TraceTree traceTree) {
      qstatesAvailable.add(traceTree);
   }

   @Override public void qstateUnavailable(final TraceTree traceTree) {
      qstatesAvailable.remove(traceTree);
   }

   public List<TraceTree> pstatesAvailable() {
      return pstatesAvailable;
   }

   public List<TraceTree> qstatesAvailable() {
      return qstatesAvailable;
   }

   public static FeatureMatcher<TraceTreeTracker, Collection<TraceTree>> hasPstatesAvailable(final Matcher<? super Collection<TraceTree>> contains) {
      return new FeatureMatcher<TraceTreeTracker, Collection<TraceTree>>(contains, "tree with p states available", "pStates available") {
         @Override protected Collection<TraceTree> featureValueOf(final TraceTreeTracker actual) {
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