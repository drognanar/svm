package com.lexicalscope.svm.partition.trace.symb.search2;

import static com.lexicalscope.svm.partition.trace.symb.tree.GoalTree.goalTree;
import static com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePairImpl.pair;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePair;
import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.search2.GoalTreeGuidedSearchStrategy;

public class TestGoalTreeGuidedSearchStrategy {
   @Rule public final ExpectedException exception = ExpectedException.none();
   @Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
   @Mock private GoalTreeCorrespondence<Object, FakeVmState> correspondence;
   @Mock private Randomiser randomiser;

   GoalTreeGuidedSearchStrategy<Object, FakeVmState> searchStrategy;

   @Before public void createStrategy() {
      searchStrategy = new GoalTreeGuidedSearchStrategy<Object, FakeVmState>(correspondence, randomiser);
   }

   @Test public void searchPThenQ() throws Exception {
      final FakeVmState pstate = new FakeVmState("p");
      final FakeVmState qstate = new FakeVmState("q");

      final GoalTreePair<Object, FakeVmState> pair = pair(goalTree(pstate), goalTree(qstate));

      context.checking(new Expectations(){{
         exactly(2).of(randomiser).random(1); will(returnValue(0));
         oneOf(correspondence).randomOpenCorrespondence(randomiser); will(returnValue(pair));
      }});
      assertThat(searchStrategy.pendingState(), equalTo(pstate));
      searchStrategy.reachedLeaf();
      assertThat(searchStrategy.pendingState(), equalTo(qstate));
   }

   @Test public void firstInitialStateIsTakenAsPSecondAsQSubsequentRejected() throws Exception {
      final FakeVmState pstate = new FakeVmState("p");
      final FakeVmState qstate = new FakeVmState("q");
      final FakeVmState spuriousstate = new FakeVmState("spurious");

      context.checking(new Expectations(){{
         oneOf(correspondence).pInitial(pstate);
         oneOf(correspondence).qInitial(qstate);
      }});

      searchStrategy.consider(pstate);
      searchStrategy.consider(qstate);

      exception.expectMessage("only 2 initial states can be considered");
      searchStrategy.consider(spuriousstate);
   }

   @Test public void forkExtendsOpenNodesOfSideBeingSearched() throws Exception {
      final FakeVmState pstate = new FakeVmState("p");
      final FakeVmState pstate1 = new FakeVmState("p1");
      final FakeVmState pstate2 = new FakeVmState("p2");

      final FakeVmState qstate = new FakeVmState("q");
      final FakeVmState qstate1 = new FakeVmState("q1");
      final FakeVmState qstate2 = new FakeVmState("q2");

      final GoalTreePair<Object, FakeVmState> pair = context.mock(GoalTreePair.class);

      context.checking(new Expectations(){{
         oneOf(correspondence).randomOpenCorrespondence(randomiser); will(returnValue(pair));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));
         oneOf(pair).expandP(new FakeVmState[]{pstate1, pstate2});
         oneOf(pair).openQNode(randomiser); will(returnValue(qstate));
         oneOf(pair).expandQ(new FakeVmState[]{qstate1, qstate2});

         oneOf(correspondence).randomOpenCorrespondence(randomiser);
      }});

      assertThat(searchStrategy.pendingState(), equalTo(pstate));
      searchStrategy.fork(new FakeVmState[]{pstate1, pstate2});

      assertThat(searchStrategy.pendingState(), equalTo(qstate));
      searchStrategy.fork(new FakeVmState[]{qstate1, qstate2});
   }
}
