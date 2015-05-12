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

   final TraceTree tt = new TraceTree();
   final TraceTreeTracker ttObserver = new TraceTreeTracker();
   {
      tt.listener(ttObserver);
   }

   @Test public void traceTreeTrackerKeepsTrackOfNodesWithAvailableStates() {
      tt.pState(state01);
      assertThat(ttObserver, hasPstatesAvailable(contains(tt)));
      tt.removePState(0);
      assertThat(ttObserver, hasPstatesAvailable(emptyCollectionOf(TraceTree.class)));
   }
}
