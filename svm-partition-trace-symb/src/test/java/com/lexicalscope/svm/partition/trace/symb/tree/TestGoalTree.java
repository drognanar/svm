package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.*;
import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.pc.Pc;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SolverRule;

public class TestGoalTree {
   @Rule public final SolverRule solver = new SolverRule();
   final GoalTree<Object> goalTree = new GoalTree<>(solver.checker());
   final Pc rootPc = new Pc();

   @Fresh ISymbol symbol;

   private Pc lessThanThree;
   private Pc moreThanMinusSix;

   @Before public void createPcs() {
      lessThanThree = rootPc.and(icmplt(symbol, asISymbol(3)));
      moreThanMinusSix = rootPc.and(icmpgt(symbol, asISymbol(-6)));
   }

   @Test public void rootHasEmptyPc() throws Exception {
      assertThat(goalTree, covers(rootPc));
   }

   @Test public void firstGoalCreatesFirstChild() throws Exception {
      goalTree.reached(new Object(), lessThanThree);
      assertThat(goalTree, hasChild(covers(lessThanThree)));
   }

//   @Test public void parentKeepsTrackOfTheCoveredSetOfItsChildre() throws Exception {
//
//      goalTree.reached(new Object(), lessThanThree);
//      goalTree.reached(new Object(), not(lessThanThree).and(moreThanMinusSix));
//
//      assertThat(goalTree, childrenCover(not(lessThanThree).and(moreThanMinusSix)).or(moreThanMinusSix)));
//   }
//
//
//   private Matcher<? super GoalTree<Object>> childrenCover(final Object and) {
//      return new TypeSafeDiagnosingMatcher<GoalTree<?>>() {
//         @Override public void describeTo(final Description description) {
//            description.appendText("node with chidren that cover").appendDescriptionOf(childMatcher);
//         }
//
//         @Override protected boolean matchesSafely(final GoalTree<?> item, final Description mismatchDescription) {
//            mismatchDescription.appendValue(item);
//            return item.hasChild(childMatcher);
//         }
//      };
//   }

   private Matcher<GoalTree<?>> hasChild(final Matcher<? super GoalTree<?>> childMatcher) {
      return new TypeSafeDiagnosingMatcher<GoalTree<?>>() {
         @Override public void describeTo(final Description description) {
            description.appendText("child matching ").appendDescriptionOf(childMatcher);
         }

         @Override protected boolean matchesSafely(final GoalTree<?> item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.hasChild(childMatcher);
         }
      };
   }

   private Matcher<GoalTree<?>> covers(final Pc pc) {
      return new TypeSafeDiagnosingMatcher<GoalTree<?>>() {
         @Override public void describeTo(final Description description) {
            description.appendText("goal tree that covers ").appendValue(pc);
         }

         @Override protected boolean matchesSafely(final GoalTree<?> item, final Description mismatchDescription) {
            mismatchDescription.appendText("goal tree that covers ").appendValue(item.pc());
            return item.covers(pc);
         }
      };
   }
}