package com.lexicalscope.svm.partition.trace.symb.search2;

import static com.lexicalscope.svm.search2.TraceTreeSearchState.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.lexicalscope.svm.search2.Side;
import com.lexicalscope.svm.search2.TraceTree;
import com.lexicalscope.svm.search2.TraceTreeSearchState;

public class TestTraceTreeSearchState {
   TraceTree tt1 = new TraceTree();
   TraceTree tt2 = new TraceTree();
   TraceTreeSearchState ss = new TraceTreeSearchState(tt1);

   @Test public void initialNodeIsRootNode() {
      assertThat(ss, currentlySearching(tt1));
      assertThat(ss, currentSideIs(Side.QSIDE));
   }

   @Test public void canSearchSomethingElse() {
      ss.search(Side.PSIDE, tt2);
      assertThat(ss, currentlySearching(tt2));
      assertThat(ss, currentSideIs(Side.PSIDE));
   }
}
