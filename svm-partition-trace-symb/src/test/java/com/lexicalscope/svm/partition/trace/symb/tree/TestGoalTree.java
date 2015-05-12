package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.*;
import static com.lexicalscope.svm.partition.trace.symb.tree.GoalTreeMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.FalseSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.symb.search.FakeVmState;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SolverRule;

public class TestGoalTree {
   @Rule public final SolverRule solver = new SolverRule();
   final GoalTree goalTree = new GoalTree(new ObjectGoalMapFactory(), solver.checker());

   @Fresh ISymbol symbol;

   final Trace goal1 = new FakeTrace();
   final Trace goal2 = new FakeTrace();

   final JState state1 = new FakeVmState();
   final JState state2 = new FakeVmState();

   private BoolSymbol betweenThreeAndFifteen;
   private BoolSymbol betweenTwentyFourAndThirty;

   @Before public void createPcs() {
      betweenThreeAndFifteen = icmpge(symbol, asISymbol(3)).and(icmple(symbol, asISymbol(15)));
      betweenTwentyFourAndThirty = icmpge(symbol, asISymbol(24)).and(icmple(symbol, asISymbol(30)));
   }

   @Test public void rootHasEmptyPc() throws Exception {
      assertThat(goalTree.covers(), solver.equivalent(new FalseSymbol()));
   }

   @Test public void firstGoalCreatesFirstChild() throws Exception {
      goalTree.reached(goal1, state1, betweenThreeAndFifteen);

      assertThat(goalTree, hasChild(covers(betweenThreeAndFifteen)));
      assertThat(goalTree, hasChild(hasOpenNode(equalTo(state1))));
   }

   @Test public void afterReachingAGoalThatGoalIsReached() throws Exception {
      goalTree.reached(goal1, state1, betweenThreeAndFifteen);

      assertThat(goalTree, hasReached(goal1));
      assertThat(goalTree, not(hasReached(goal2)));
      assertThat(goalTree.childForGoal(goal1), covers(betweenThreeAndFifteen));
   }

   @Test public void parentKeepsTrackOfTheCoveredSetOfItsChildren() throws Exception {
      goalTree.reached(goal1, state1, betweenTwentyFourAndThirty);
      goalTree.reached(goal2, state2, betweenThreeAndFifteen);

      assertThat(goalTree, both(childrenCover(betweenThreeAndFifteen))
                           .and(childrenCover(betweenTwentyFourAndThirty)));
   }

   @Test public void reachingGoalTwoWaysUpdatesCoveredSetOfItsChildre() throws Exception {
      goalTree.reached(goal1, state1, betweenThreeAndFifteen);
      goalTree.reached(goal1, state2, betweenTwentyFourAndThirty);

      assertThat(goalTree, childrenCover(betweenThreeAndFifteen.or(betweenTwentyFourAndThirty)));
      assertThat(goalTree, hasChild(
             both(covers(betweenThreeAndFifteen))
             .and(covers(betweenTwentyFourAndThirty))));

      assertThat(goalTree,
            hasChild(both(hasOpenNode(equalTo(state1))).and(hasOpenNode(equalTo(state2)))));
   }
}
