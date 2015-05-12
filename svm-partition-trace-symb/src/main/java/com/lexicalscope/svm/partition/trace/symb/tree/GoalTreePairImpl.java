package com.lexicalscope.svm.partition.trace.symb.tree;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.and;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.search.Randomiser;
import com.lexicalscope.svm.vm.j.JState;

public class GoalTreePairImpl implements GoalTreePair {
   final GoalTree pside;
   final GoalTree qside;

   public GoalTreePairImpl(final GoalTree pside, final GoalTree qside) {
      this.pside = pside;
      this.qside = qside;
   }

   @Override public boolean isOpen() {
      return psideIsOpen() || qsideIsOpen();
   }

   @Override public boolean qsideIsOpen() {
      return qside.isOpen();
   }

   @Override public boolean psideIsOpen() {
      return pside.isOpen();
   }

   @Override public boolean covers(final BoolSymbol pc) {
      return pside.covers(pc) || qside.covers(pc);
   }

   @Override public BoolSymbol pc() {
      // TODO[tim]: is this right? what does it mean?
      return and(pside.pc(), qside.pc());
   }

   @Override
   public JState openPNode(final Randomiser randomiser) {
      return pside.randomOpenNode(randomiser);
   }

   @Override
   public JState openQNode(final Randomiser randomiser) {
      return qside.randomOpenNode(randomiser);
   }

   @Override public GoalTree pside() {
      return pside;
   }

   @Override public GoalTree qside() {
      return qside;
   }

   public static GoalTreePair pair(
         final GoalTree pside,
         final GoalTree qside) {
      return new GoalTreePairImpl(pside, qside);
   }

   @Override public void expandP(final JState[] states) {
      for (final JState s : states) {
         pside.increaseOpenNodes(s);
      }
   }

   @Override public void expandQ(final JState[] states) {
      for (final JState s : states) {
         qside.increaseOpenNodes(s);
      }
   }

   @Override public String toString() {
      return String.format("(pair %s %s)", pside, qside);
   }
}