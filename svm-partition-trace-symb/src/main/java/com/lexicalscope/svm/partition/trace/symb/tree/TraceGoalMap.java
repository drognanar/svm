package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.partition.trace.symb.SymbolicTraceMatchers.equivalentTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public final class TraceGoalMap<N> extends AbstractGoalMap<N> {
   private static final class Child<N> {
      private final Trace goal;
      private final N node;

      public Child(final Trace goal, final N node) {
         this.goal = goal;
         this.node = node;
      }

      public Trace goal() { return goal; }
      public N node() { return node; }

      @Override public String toString() {
         return String.format("(child %s %s)", goal, node);
      }
   }

   private final List<Child<N>> children = new ArrayList<>();
   private final FeasibilityChecker checker;

   public TraceGoalMap(final FeasibilityChecker checker) {
      this.checker = checker;
   }

   @Override public void put(final Trace goal, final N node) {
      children.add(new Child<N>(goal, node));
   }

   @Override public N get(final Trace goal) {
      // TODO[tim]: we already know the trace prefixes match - try to take advantage of that
      final Matcher<Trace> equivalentTo = equivalentTo(checker, goal);
      for (final Child<N> child : children) {
         if(equivalentTo.matches(child.goal())) {
            return child.node();
         }
      }
      return null;
   }

   @Override public N get(final Trace goal, final com.lexicalscope.svm.partition.trace.symb.tree.GoalMap.SubtreeFactory<N> factory) {
      N result = get(goal);
      if(result == null) {
         result = factory.create();
         put(goal, result);
      }
      return result;
   }

   @Override public boolean containsGoal(final Trace goal) {
      return get(goal) != null;
   }

   @Override public int size() {
      return children.size();
   }

   @Override public boolean isEmpty() {
      return children.isEmpty();
   }

   @Override public Iterator<N> iterator() {
      final Iterator<Child<N>> iterator = children.iterator();
      return new Iterator<N>() {
         @Override public boolean hasNext() {
            return iterator.hasNext();
         }

         @Override public N next() {
            return iterator.next().node();
         }

         @Override public void remove() {
            iterator.remove();
         }
      };
   }

   @Override public Collection<N> values() {
      final List<N> result = new ArrayList<>(children.size());
      for (final Child<N> child : children) {
         result.add(child.node);
      }
      return result;
   }

   @Override public String toString() {
      return String.format("(GoalMap %s)", children);
   }
}
