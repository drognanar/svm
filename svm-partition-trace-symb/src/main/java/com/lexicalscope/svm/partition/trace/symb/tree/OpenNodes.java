package com.lexicalscope.svm.partition.trace.symb.tree;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public final class OpenNodes {
   private final FeasibilityChecker feasibilityChecker;
   private final List<JState> nodes = new ArrayList<>();

   public OpenNodes(final FeasibilityChecker feasibilityChecker) {
      this.feasibilityChecker = feasibilityChecker;
   }

   public void push(final JState node) {
      nodes.add(node);
   }

   public JState pop() {
      return nodes.remove(0);
   }

   public boolean hasMatching(final Matcher<JState> nodeMatcher) {
      for (final JState node : nodes) {
         if(nodeMatcher.matches(node)) {
            return true;
         }
      }
      return false;
   }

   public boolean isEmpty() {
      return nodes.isEmpty();
   }

   public JState random(final Randomiser randomiser) {
      assert !nodes.isEmpty();

      return nodes.remove(randomiser.random(nodes.size()));
   }

   @Override public String toString() {
      return format("(open %s)", nodes);
   }
}
