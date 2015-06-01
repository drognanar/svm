package com.lexicalscope.svm.vm.search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import com.lexicalscope.svm.vm.StateSearch;

public class DepthFirstStateSearch<S> implements StateSearch<S> {
   private final List<Deque<S>> pending = new ArrayList<>();
   private final Deque<S> finished = new ArrayDeque<>();
   private int current = 0;

   @Override
   public S firstResult() {
      return finished.isEmpty() ? null : finished.peek();
   }

   @Override
   public void reachedLeaf() {
      finished.push(pending().pop());
      if(pending().isEmpty()) {
         pending.remove(current);
         if(!pending.isEmpty()) {
            current = current % pending.size();
         }
      } else {
         roundRobin();
      }
   }

   @Override
   public S pendingState() {
      return pending().peek();
   }

   @Override
   public void fork(final S parent, final S[] states) {
      final S popped = pending().pop();
      assert popped == parent;
      for (final S state : states) {
         pending().push(state);
      }
      roundRobin();
   }

   @Override
   public void forkDisjoined(final S parent, final S[] states) {
      fork(parent, states);
   }

   private void roundRobin() {
      current = (current + 1) % pending.size();
   }

   @Override
   public void consider(final S state) {
      final ArrayDeque<S> pendingStack = new ArrayDeque<>();
      pendingStack.push(state);
      pending.add(pendingStack);
   }

   @Override
   public Collection<S> results() {
      return finished;
   }

   @Override public void goal() {
      roundRobin();
   }

   private Deque<S> pending() {
      if(pending.isEmpty()) {
         return new ArrayDeque<>();
      }
      return pending.get(current);
   }
}