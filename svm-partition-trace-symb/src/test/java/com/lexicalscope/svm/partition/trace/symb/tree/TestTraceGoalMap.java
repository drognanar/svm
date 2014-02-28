package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeMatchers.nodeCount;
import static com.lexicalscope.svm.search.ConstantRandomiser.constant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.partition.trace.TraceBuilder;
import com.lexicalscope.svm.vm.symb.junit.SolverRule;

public class TestTraceGoalMap {
   @Rule public final SolverRule solver = new SolverRule();
   private TraceGoalMap<Object> traceGoalMap;

   private final Object node = new Object();
   private final TraceBuilder traceFoo = trace().methodCall(Object.class, "foo", "()V", new Object());

   @Before public void createMap() {
      traceGoalMap = new TraceGoalMap<Object>(solver.checker());
   }

   @Test public void startsEmpty() throws Exception {
      assertThat("starts empty", traceGoalMap.isEmpty());
   }

   @Test public void addMakesNotEmpty() throws Exception {
      traceGoalMap.put(trace().build(), new Object());
      assertThat("starts empty", !traceGoalMap.isEmpty());
   }

   @Test public void addingTwoGivesSizeTwo() throws Exception {
      traceGoalMap.put(trace().build(), new Object());
      traceGoalMap.put(traceFoo.build(), new Object());

      assertThat(traceGoalMap, nodeCount(2));
   }

   @Test public void canRetrieveGoalByEmptyTraceEquivalence() throws Exception {
      traceGoalMap.put(trace().build(), node);
      assertThat(traceGoalMap.get(trace().build()), sameInstance(node));
   }

   @Test public void containsGoalByEmptyTraceEquivalence() throws Exception {
      traceGoalMap.put(trace().build(), node);
      assertThat("contains by equivalence", traceGoalMap.containsGoal(trace().build()));
   }

   @Test public void emptyTraceNotEqualToItself() throws Exception {
      assertThat(trace().build(), not(equalTo(trace().build())));
   }

   @Test public void canPickNodesAtRandom() throws Exception {
      final Object node1 = new Object();
      final Object node2 = new Object();
      traceGoalMap.put(trace().build(), node1);
      traceGoalMap.put(traceFoo.build(), node2);

      assertThat(traceGoalMap.getRandom(constant(0)), equalTo(node1));
      assertThat(traceGoalMap.getRandom(constant(1)), equalTo(node2));
   }
}

