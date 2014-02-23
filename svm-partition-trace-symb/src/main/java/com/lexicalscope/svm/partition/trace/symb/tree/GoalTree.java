package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.google.common.base.Joiner.on;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.FalseSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class GoalTree<T, S> {
   private final Map<T, GoalTree<T, S>> children = new HashMap<>();
   private final OpenNodes<S> openNodes;
   private final FeasibilityChecker feasibilityChecker;
   private BoolSymbol coveredPc;
   private BoolSymbol childrenCoverPc;

   public GoalTree() {
      this(new FeasibilityChecker());
   }

   public GoalTree(final FeasibilityChecker feasibilityChecker) {
      this.feasibilityChecker = feasibilityChecker;
      this.coveredPc = new TrueSymbol();
      this.childrenCoverPc = new FalseSymbol();
      this.openNodes = new OpenNodes<>(feasibilityChecker);
   }

   public BoolSymbol pc() {
      return coveredPc;
   }

   public boolean covers(final BoolSymbol pc) {
      return feasibilityChecker.implies(pc, coveredPc);
   }

   private void increaseCovers(final BoolSymbol conjunct) {
      coveredPc = coveredPc.or(conjunct);
   }

   public void reached(final T goal, final S state, final BoolSymbol childPc) {
      childrenCoverPc = childrenCoverPc.or(childPc);

      final GoalTree<T, S> child;
      if(children.containsKey(goal)) {
         child = children.get(goal);
      } else {
         child = new GoalTree<T, S>(feasibilityChecker);
         children.put(goal, child);
      }
      child.increaseCovers(childPc);
      child.increaseOpenNodes(state);
   }

   private void increaseOpenNodes(final S state) {
      openNodes.push(state);
   }

   public boolean hasChild(final Matcher<? super GoalTree<T, S>> childMatcher) {
      for (final GoalTree<T, S> child : children.values()) {
         if(childMatcher.matches(child)) {
            return true;
         }
      }
      return false;
   }

   public boolean hasOpenNode(final Matcher<S> openNodeMatcher) {
      return openNodes.hasMatching(openNodeMatcher);
   }

   public boolean childrenCover(final BoolSymbol pc) {
      return feasibilityChecker.implies(pc, childrenCoverPc);
   }

   public GoalTree<T, S> child(final T goal) {
      return children.get(goal);
   }

   @Override public String toString() {
      return format("(node (covers %s) (children cover %s) %s %s)",
               coveredPc,
               childrenCoverPc,
               openNodes,
               on(" ").join(children.values()));
   }
}