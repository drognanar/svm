package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.truth;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.search.ListRandomPool;
import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;


public final class GoalTreeCorrespondenceImpl implements GoalTreeCorrespondence {
   // should be flat not tree
   // map should map to a pair, each trace should reach one GoalTree in each side
   // this also makes it easier to find a place to search from...
   private final GoalMap<GoalTreePair> children;
   private final ListRandomPool<GoalTreePair> openChildren;
   private final GoalTree pside;
   private final GoalTree qside;

   private GoalTreeCorrespondenceImpl(
         final Trace rootGoal,
         final GoalTree pside,
         final GoalTree qside,
         final GoalMapFactory goalMapFactory) {
      this.pside = pside;
      this.qside = qside;
      children = goalMapFactory.newGoalMap();
      openChildren = new ListRandomPool<GoalTreePair>();
      put(rootGoal, new GoalTreePairImpl(pside, qside));
   }

   private void put(final Trace goal, final GoalTreePair node) {
      children.put(goal, node);
      openChildren.add(node);
   }

   @Override public void stillOpen(final GoalTreePair node) {
      openChildren.add(node);
   }

   @Override public GoalTreePair randomOpenChild(final Randomiser randomiser) {
      return openChildren.randomElement(randomiser);
   }

   @Override public boolean hasOpenChildren() {
      return !openChildren.isEmpty();
   }

   @Override public boolean hasChildren() {
      return !children.isEmpty();
   }

   @Override
   public boolean isOpen() {
      for (final GoalTreePair child : children) {
         if(child.isOpen()) {
            return true;
         }
      }
      return false;
   }

   @Override
   public GoalTreePair reachedP(
         final GoalTreePair parent,
         final Trace goal,
         final JState state,
         final BoolSymbol childPc) {
      return reached(goal, state, childPc, new PqChildFactory(), parent.pside(), parent.qside());
   }

   @Override
   public GoalTreePair reachedQ(
         final GoalTreePair parent,
         final Trace goal,
         final JState state,
         final BoolSymbol childPc) {
      return reached(goal, state, childPc, new QpChildFactory(), parent.qside(), parent.pside());
   }

   private GoalTreePair reached(
         final Trace goal,
         final JState state,
         final BoolSymbol childPc,
         final ChildFactory childFactory,
         final GoalTree thisSide,
         final GoalTree otherSide) {
      final boolean reachedBefore = thisSide.hasReached(goal);

      final GoalTree thisSideChild = thisSide.reached(goal, state, childPc);
      assert thisSide.overlappingChildGoals(childPc).size() == 1 : this;

      final List<GoalTree> otherSideOverlappingChildGoals =
            otherSide.overlappingChildGoals(childPc);
      if(otherSideOverlappingChildGoals.size() > 1||
            otherSideOverlappingChildGoals.size() == 1 &&
            !otherSide.isChildForGoal(otherSideOverlappingChildGoals.get(0), goal)) {
         if(otherSideOverlappingChildGoals.size() == 1) {
            throw new RuntimeException(String.format("unbounded%n\t%s%n\t%s", goal, otherSideOverlappingChildGoals.get(0)));
         } else {
            throw new RuntimeException(String.format("unbounded%n\t%s%n\t%s", goal, otherSideOverlappingChildGoals));
         }
      }

      if(!reachedBefore && otherSide.hasReached(goal)){
         final GoalTreePair result = childFactory.create(thisSideChild, otherSide.childForGoal(goal));
         put(goal, result);
         return result;
      }
      return null;
   }

   private interface ChildFactory {
      GoalTreePair create(GoalTree thisSide, GoalTree otherSide);
   }

   private static class PqChildFactory implements ChildFactory {
      @Override public GoalTreePair create(
            final GoalTree thisSide,
            final GoalTree otherSide) {
         return new GoalTreePairImpl(thisSide, otherSide);
      }
   }

   private static class QpChildFactory implements ChildFactory {
      @Override public GoalTreePair create(final GoalTree thisSide, final GoalTree otherSide) {
         return new GoalTreePairImpl(otherSide, thisSide);
      }
   }

   @Override public String toString() {
      return String.format("(correspondence %s)", children);
   }

   @Override public void pInitial(final JState pstate0) {
      pside.increaseOpenNodes(pstate0);
   }

   @Override public void qInitial(final JState qstate0) {
      qside.increaseOpenNodes(qstate0);
   }

   @Override public boolean hasChild(final Matcher<? super GoalTreePair> childMatcher) {
      return children.containsMatching(childMatcher);
   }

   @Override
   public int childCount() {
      return children.size();
   }

   @Override public GoalTreePair correspondence(final Trace goal) {
      return children.get(goal);
   }

   @Override public Iterator<GoalTreePair> iterator() {
      return children.iterator();
   }

   @Override public Collection<GoalTreePair> children() {
      return children.values();
   }

   public static GoalTreeCorrespondence root(
         final Trace rootGoal,
         final FeasibilityChecker feasibilityChecker,
         final GoalMapFactory goalMapFactory) {
      final GoalTree pside = new GoalTree(goalMapFactory, feasibilityChecker);
      final GoalTree qside = new GoalTree(goalMapFactory, feasibilityChecker);
      pside.covers(truth());
      qside.covers(truth());
      return new GoalTreeCorrespondenceImpl(rootGoal, pside, qside, goalMapFactory);
   }

   public static GoalTreeCorrespondence root(
         final Trace rootGoal,
         final JState pstate0,
         final JState qstate0,
         final FeasibilityChecker feasibilityChecker,
         final GoalMapFactory goalMapFactory) {
      final GoalTreeCorrespondence result =
            root(rootGoal, feasibilityChecker, goalMapFactory);

      result.pInitial(pstate0);
      result.qInitial(qstate0);

      return result;
   }
}
