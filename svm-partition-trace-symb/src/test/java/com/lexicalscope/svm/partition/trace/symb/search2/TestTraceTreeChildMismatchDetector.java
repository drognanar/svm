package com.lexicalscope.svm.partition.trace.symb.search2;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.*;
import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static com.lexicalscope.svm.search2.TraceTreeChildMismatchDetector.hasMismatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.search2.TraceTree;
import com.lexicalscope.svm.search2.TraceTreeChildMismatchDetector;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SolverRule;

public class TestTraceTreeChildMismatchDetector {
   @Rule public final SolverRule solver = new SolverRule();

   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
   @Mock public JState pstate01;
   @Mock public JState pstate02;
   @Mock public JState qstate01;
   @Mock public JState qstate02;

   @Fresh ISymbol symbol;

   final Trace trace1 = trace().methodCall(Object.class, "<init>", "()V", new Object()).build();
   final Trace trace2 = trace().methodCall(String.class, "<init>", "()V", new Object()).build();

   final TraceTree tree = new TraceTree();
   final TraceTree child01 = tree.child(trace1);
   final TraceTree child02 = tree.child(trace2);

   private BoolSymbol betweenZeroAndFour;
   private BoolSymbol betweenFiveAndTen;

   private BoolSymbol betweenZeroAndFive;
   private BoolSymbol betweenSixAndTen;

   private BoolSymbol betweenSevenAndTen;

   TraceTreeChildMismatchDetector mismatchDetector;


   @Before public void setup() {
      mismatchDetector = new TraceTreeChildMismatchDetector(solver.checker());
   }

   @Before public void createPcs() {
      betweenZeroAndFour = icmpge(symbol, asISymbol(0)).and(icmple(symbol, asISymbol(4)));
      betweenFiveAndTen = icmpge(symbol, asISymbol(5)).and(icmple(symbol, asISymbol(10)));

      betweenZeroAndFive = icmpge(symbol, asISymbol(0)).and(icmple(symbol, asISymbol(5)));
      betweenSixAndTen = icmpge(symbol, asISymbol(6)).and(icmple(symbol, asISymbol(10)));

      betweenSevenAndTen = icmpge(symbol, asISymbol(7)).and(icmple(symbol, asISymbol(10)));
   }

   @Test public void childrenWithOverlappingPathConditionsIsAMismatch() {
      child01.pState(pstate01);
      child01.disjoinP(betweenZeroAndFour);
      child02.pState(pstate02);
      child02.disjoinP(betweenFiveAndTen);

      child01.qState(qstate01);
      child01.disjoinQ(betweenZeroAndFive);
      child02.qState(qstate02);
      child02.disjoinQ(betweenSixAndTen);

      assertThat(tree, hasMismatch(mismatchDetector));
   }

   @Test public void childrenWithIdenticalPathConditionsIsOK() {
      child01.pState(pstate01);
      child01.disjoinP(betweenZeroAndFour);
      child02.pState(pstate02);
      child02.disjoinP(betweenFiveAndTen);

      child01.qState(qstate01);
      child01.disjoinQ(betweenZeroAndFour);
      child02.qState(qstate02);
      child02.disjoinQ(betweenFiveAndTen);

      assertThat(tree, not(hasMismatch(mismatchDetector)));
   }

   @Test public void childrenWithSubsetPathConditionsIsOK() {
      child01.pState(pstate01);
      child01.disjoinP(betweenZeroAndFour);
      child02.pState(pstate02);
      child02.disjoinP(betweenFiveAndTen);

      child01.qState(qstate01);
      child01.disjoinQ(betweenZeroAndFour);
      child02.qState(qstate02);
      child02.disjoinQ(betweenSevenAndTen);

      assertThat(tree, not(hasMismatch(mismatchDetector)));
   }
}
