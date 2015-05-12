package com.lexicalscope.svm.partition.trace.symb.tree;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.vm.j.JState;

public class GoalTreeMatchers {
   public static Matcher<GoalTree> childrenCover(final BoolSymbol pc) {
      return new TypeSafeDiagnosingMatcher<GoalTree>() {
         @Override public void describeTo(final Description description) {
            description.appendText("node with chidren that cover ").appendValue(pc);
         }

         @Override protected boolean matchesSafely(final GoalTree item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.childrenCover(pc);
         }
      };
   }

   public static <T, S> Matcher<GoalTree> hasChild(final Matcher<? super GoalTree> childMatcher) {
      return new TypeSafeDiagnosingMatcher<GoalTree>() {
         @Override public void describeTo(final Description description) {
            description.appendText("child matching ").appendDescriptionOf(childMatcher);
         }

         @Override protected boolean matchesSafely(final GoalTree item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.hasChild(childMatcher);
         }
      };
   }

   public static <S> Matcher<GoalTree> hasOpenNode(final Matcher<JState> childMatcher) {
      return new TypeSafeDiagnosingMatcher<GoalTree>() {
         @Override public void describeTo(final Description description) {
            description.appendText("child matching ").appendDescriptionOf(childMatcher);
         }

         @Override protected boolean matchesSafely(final GoalTree item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.hasOpenNode(childMatcher);
         }
      };
   }

   public static <T> Matcher<GoalTree> hasReached(final Trace goal) {
      return new TypeSafeDiagnosingMatcher<GoalTree>() {
         @Override public void describeTo(final Description description) {
            description.appendText("reached sub goal ").appendValue(goal);
         }

         @Override protected boolean matchesSafely(
               final GoalTree item,
               final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.hasReached(goal);
         }
      };
   }

   public static Matcher<InputSubset> covers(final BoolSymbol pc) {
      return new TypeSafeDiagnosingMatcher<InputSubset>() {
         @Override public void describeTo(final Description description) {
            description.appendText("goal tree that covers ").appendValue(pc);
         }

         @Override protected boolean matchesSafely(final InputSubset item, final Description mismatchDescription) {
            mismatchDescription.appendText("goal tree that covers ").appendValue(item.pc());
            return item.covers(pc);
         }
      };
   }

   public static Matcher<? super GoalTreeCorrespondence> hasCorrespondence(final Matcher<? super GoalTreePair> childMatcher) {
      return new TypeSafeDiagnosingMatcher<GoalTreeCorrespondence>() {
         @Override public void describeTo(final Description description) {
            description.appendText("correspondence with child matching ").appendDescriptionOf(childMatcher);
         }

         @Override protected boolean matchesSafely(final GoalTreeCorrespondence item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.hasChild(childMatcher);
         }
      };
   }

   public static Matcher<? super GoalTreeCorrespondence> hasCorrespondences(final int count) {
      return new TypeSafeDiagnosingMatcher<GoalTreeCorrespondence>() {
         @Override public void describeTo(final Description description) {
            description.appendText("correspondence with child count ").appendValue(count);
         }

         @Override protected boolean matchesSafely(
               final GoalTreeCorrespondence item,
               final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.childCount() == count;
         }
      };
   }

   public static Matcher<? super GoalTreeCorrespondence> isOpen() {
      return new TypeSafeDiagnosingMatcher<GoalTreeCorrespondence>(){
         @Override public void describeTo(final Description description) {
            description.appendText("correspondence with open nodes");
         }

         @Override protected boolean matchesSafely(final GoalTreeCorrespondence item, final Description mismatchDescription) {
            final boolean result = item.isOpen();
            if(!result) {
               mismatchDescription.appendValue(item);
            }
            return result;
         }};
   }

   public static Matcher<? super GoalMap<?>> nodeCount(final int count) {
      return new TypeSafeDiagnosingMatcher<GoalMap<?>>(){
         @Override public void describeTo(final Description description) {
            description.appendText("goal map with size ").appendValue(count);
         }

         @Override protected boolean matchesSafely(final GoalMap<?> item, final Description mismatchDescription) {
            final boolean result = item.size() == count;
            if(!result) {
               mismatchDescription.appendValue(item);
            }
            return result;
         }};
   }

}
