package com.lexicalscope.svm.partition.trace.symb.search2;

import static com.lexicalscope.svm.search2.TraceTreeTracker.hasPstatesAvailable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.search2.TraceTree;
import com.lexicalscope.svm.search2.TraceTreeTracker;
import com.lexicalscope.svm.vm.j.JState;

public class TestTraceTreeTracker {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
   @Mock public JState state01;
   @Mock public JState state02;

   final TraceTreeTracker ttObserver = new TraceTreeTracker();
   final TraceTree tt = new TraceTree(ttObserver);

   @Test public void traceTreeTrackerKeepsTrackOfNodesWithAvailableStates() {
      tt.pState(state01);
      assertThat(ttObserver, hasPstatesAvailable(contains(tt)));
      tt.pStates().pickState();
      assertThat(ttObserver, hasPstatesAvailable(emptyCollectionOf(TraceTree.class)));
   }
}
