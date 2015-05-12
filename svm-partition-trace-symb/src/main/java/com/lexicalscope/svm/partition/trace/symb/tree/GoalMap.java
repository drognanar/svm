package com.lexicalscope.svm.partition.trace.symb.tree;

import java.util.Collection;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.partition.trace.Trace;

public interface GoalMap<N> extends Iterable<N> {
   public interface SubtreeFactory<N> { N create(); }

   void put(Trace goal, N node);
   N get(Trace goal);
   N get(Trace goal, SubtreeFactory<N> factory);

   boolean containsMatching(Matcher<? super N> childMatcher);
   boolean containsGoal(Trace goal);
   boolean isChildForGoal(N child, Trace goal);

   Collection<N> values();
   int size();
   boolean isEmpty();
}
