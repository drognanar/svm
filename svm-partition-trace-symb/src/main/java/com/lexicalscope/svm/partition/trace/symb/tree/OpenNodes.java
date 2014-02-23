package com.lexicalscope.svm.partition.trace.symb.tree;

import static java.lang.String.format;

import java.util.LinkedList;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.z3.FeasibilityChecker;

public class OpenNodes<S> {
   private final FeasibilityChecker feasibilityChecker;
   private final LinkedList<S> nodes = new LinkedList<>();

   public OpenNodes(final FeasibilityChecker feasibilityChecker) {
      this.feasibilityChecker = feasibilityChecker;
   }

   public void push(final S node) {
      nodes.add(node);
   }

   public S pop() {
      return nodes.remove(0);
   }

   public boolean hasMatching(final Matcher<S> nodeMatcher) {
      for (final S node : nodes) {
         if(nodeMatcher.matches(node)) {
            return true;
         }
      }
      return false;
   }

   @Override public String toString() {
      return format("(open %s)", nodes);
   }
}