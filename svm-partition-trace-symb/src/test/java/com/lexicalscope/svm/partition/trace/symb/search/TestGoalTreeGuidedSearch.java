package com.lexicalscope.svm.partition.trace.symb.search;

import static com.lexicalscope.MatchersAdditional.has;
import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.truth;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.symb.tree.FakeTrace;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeCorrespondence;
import com.lexicalscope.svm.partition.trace.symb.tree.GoalTreePair;
import com.lexicalscope.svm.search.GoalTreeGuidedSearch;
import com.lexicalscope.svm.search.NullGuidedSearchObserver;
import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.search.SearchMetaExtractor;
import com.lexicalscope.svm.vm.j.JState;

public class TestGoalTreeGuidedSearch {
   @Rule public final ExpectedException exception = ExpectedException.none();
   @Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
   @Mock private GoalTreeCorrespondence correspondence;
   @Mock private Randomiser randomiser;
   @Mock private GoalTreePair pair;
   @Mock private SearchMetaExtractor goalExtractor;

   final JState pstate = new FakeVmState("p");
   final JState pstate1 = new FakeVmState("p1");
   final JState pstate2 = new FakeVmState("p2");

   final JState qstate = new FakeVmState("q");
   final JState qstate1 = new FakeVmState("q1");
   final JState qstate2 = new FakeVmState("q2");

   GoalTreeGuidedSearch searchStrategy;

   @Before public void createStrategy() {
      searchStrategy = new GoalTreeGuidedSearch(
            new NullGuidedSearchObserver(),
            correspondence,
            goalExtractor,
            randomiser);
   }

   @Test public void searchPThenQ() throws Exception {
//      final GoalTreePair<Object, FakeVmState> pair = pair(goalTree(pstate), goalTree(qstate));

      context.checking(new Expectations(){{
         oneOf(correspondence).randomOpenChild(randomiser); will(returnValue(pair));

         oneOf(pair).psideIsOpen(); will(returnValue(true));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));

         oneOf(pair).qsideIsOpen(); will(returnValue(true));
         oneOf(pair).openQNode(randomiser); will(returnValue(qstate));
      }});
      assertThat(searchStrategy.pendingState(), equalTo(pstate));
      searchStrategy.reachedLeaf();
      assertThat(searchStrategy.pendingState(), equalTo(qstate));
   }

   @Test public void firstInitialStateIsTakenAsPSecondAsQSubsequentRejected() throws Exception {
      final FakeVmState spuriousstate = new FakeVmState("spurious");

      context.checking(new Expectations(){{
         oneOf(goalExtractor).configureInitial(pstate);
         oneOf(correspondence).pInitial(pstate);

         oneOf(goalExtractor).configureInitial(qstate);
         oneOf(correspondence).qInitial(qstate);
      }});

      searchStrategy.consider(pstate);
      searchStrategy.consider(qstate);

      exception.expectMessage("only 2 initial states can be considered");
      searchStrategy.consider(spuriousstate);
   }

   @Test public void forkExtendsOpenNodesOfSideBeingSearched() throws Exception {
      context.checking(new Expectations(){{
         oneOf(correspondence).randomOpenChild(randomiser); will(returnValue(pair));
         oneOf(pair).isOpen(); will(returnValue(true));
         oneOf(pair).psideIsOpen(); will(returnValue(true));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));
      }});

      assertThat(searchStrategy.pendingState(), equalTo(pstate));

      context.checking(new Expectations(){{
         oneOf(pair).expandP(new JState[]{pstate1, pstate2});
         oneOf(pair).qsideIsOpen(); will(returnValue(true));
         oneOf(pair).openQNode(randomiser); will(returnValue(qstate));
      }});
      searchStrategy.fork(null, new JState[]{pstate1, pstate2});
      assertThat(searchStrategy.pendingState(), equalTo(qstate));

      context.checking(new Expectations(){{
         oneOf(pair).expandQ(new JState[]{qstate1, qstate2});

         oneOf(correspondence).stillOpen(pair);

         oneOf(correspondence).hasOpenChildren(); will(returnValue(true));
         oneOf(correspondence).randomOpenChild(randomiser); will(returnValue(pair));
         oneOf(pair).psideIsOpen(); will(returnValue(true));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));
      }});

      searchStrategy.fork(null, new JState[]{qstate1, qstate2});
   }

   @Test public void resultCreatedAtLeaf() throws Exception {
      context.checking(new Expectations(){{
         oneOf(correspondence).randomOpenChild(randomiser); will(returnValue(pair));
         oneOf(pair).psideIsOpen(); will(returnValue(true));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));

         oneOf(pair).qsideIsOpen(); will(returnValue(true));
         oneOf(pair).openQNode(randomiser); will(returnValue(qstate));
      }});

      assertThat(searchStrategy.pendingState(), equalTo(pstate));
      searchStrategy.reachedLeaf();
      assertThat(searchStrategy.firstResult(), equalTo(pstate));
      assertThat(searchStrategy.results(), has(equalTo(pstate)).only().inOrder());
   }

   @Test @Ignore("These tests are illegible") public void goalIsNotifiedToCorrespondence() throws Exception {
      final Trace goal = new FakeTrace();

      context.checking(new Expectations(){{
         oneOf(correspondence).randomOpenChild(randomiser); will(returnValue(pair));
         oneOf(pair).psideIsOpen(); will(returnValue(true));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));
      }});
      assertThat(searchStrategy.pendingState(), equalTo(pstate));

      context.checking(new Expectations(){{
         oneOf(correspondence).reachedP(pair, goal, pstate, truth());
         oneOf(goalExtractor).goal(pstate); will(returnValue(goal));
         oneOf(goalExtractor).pc(pstate); will(returnValue(truth()));

         oneOf(pair).qsideIsOpen(); will(returnValue(true));
         oneOf(pair).openQNode(randomiser); will(returnValue(qstate));
      }});

      searchStrategy.goal();
      assertThat(searchStrategy.pendingState(), equalTo(qstate));

      context.checking(new Expectations(){{
         oneOf(correspondence).reachedQ(pair, goal, qstate, truth());
         oneOf(goalExtractor).goal(qstate); will(returnValue(goal));
         oneOf(goalExtractor).pc(qstate); will(returnValue(truth()));

         oneOf(pair).isOpen(); will(returnValue(true));
         oneOf(correspondence).stillOpen(pair);
         oneOf(correspondence).hasOpenChildren(); will(returnValue(true));
         oneOf(correspondence).randomOpenChild(randomiser); will(returnValue(pair));
         oneOf(pair).psideIsOpen(); will(returnValue(true));
         oneOf(pair).openPNode(randomiser); will(returnValue(pstate));
      }});
      searchStrategy.goal();
   }
}

