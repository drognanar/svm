package com.lexicalscope.svm.partition.trace.symb.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.lexicalscope.svm.partition.trace.Trace;

public final class ObjectGoalMap<N> extends AbstractGoalMap<N> {
   private final LinkedHashMap<Trace, N> childMap = new LinkedHashMap<>();
   private final List<N> list = new ArrayList<>();

   @Override public N get(final Trace goal, final SubtreeFactory<N> factory) {
      N child = childMap.get(goal);
      if(child == null) {
         child = factory.create();
         childMap.put(goal, child);
      }
      return child;
   }

   @Override public void put(final Trace goal, final N node) {
      list.add(node);
      childMap.put(goal, node);
   }

   @Override public boolean isEmpty() {
      return childMap.isEmpty();
   }

   @Override public Iterator<N> iterator() {
      return childMap.values().iterator();
   }

   @Override public int size() {
      return childMap.size();
   }

   @Override public boolean containsGoal(final Trace goal) {
      return childMap.containsKey(goal);
   }

   @Override public N get(final Trace goal) {
      return childMap.get(goal);
   }

   @Override public Collection<N> values() {
      return childMap.values();
   }
}
