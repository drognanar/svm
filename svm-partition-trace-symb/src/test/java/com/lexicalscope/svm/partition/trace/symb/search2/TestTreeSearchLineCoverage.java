package com.lexicalscope.svm.partition.trace.symb.search2;

import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.*;
import static com.lexicalscope.svm.partition.spec.MatchersSpec.*;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;
import static com.lexicalscope.svm.search2.Side.*;
import static com.lexicalscope.svm.vm.j.StateMatchers.*;
import static com.lexicalscope.svm.vm.j.code.AsmSMethodName.*;
import static com.lexicalscope.svm.vm.symb.matchers.SymbStateMatchers.pcIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.junit.Assert.fail;
import static org.objectweb.asm.Type.getInternalName;

import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Auto;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.loop.broken.LoopInsidePartition;
import com.lexicalscope.svm.examples.loop.broken.LoopOutsidePartition;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.search.ConstantRandomiser;
import com.lexicalscope.svm.search.GuidedSearchObserver;
import com.lexicalscope.svm.search2.LineCoverageStatesCollectionFactory;
import com.lexicalscope.svm.search2.PartitionViolationException;
import com.lexicalscope.svm.search2.TreeSearchFactory;
import com.lexicalscope.svm.search2.TreeSearchStateSelectionRandom;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestTreeSearchLineCoverage {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
   public GuidedSearchObserver searchObserver = context.mock(GuidedSearchObserver.class);
   @Auto public Sequence searchSequence;

   @Rule public final ExpectedException exception = ExpectedException.none();
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(outsidePartition(), insidePartition(), vm);
      vm.entryPoint(LoopOutsidePartition.class, "entryPoint", "(I)V");
      vm.builder().searchWith(new TreeSearchFactory(searchObserver, vm.feasbilityChecker(), new TreeSearchStateSelectionRandom(new ConstantRandomiser(0), new LineCoverageStatesCollectionFactory())));
   }

   public ISymbol symbol = new ITerminalSymbol("s");

   public Matcher<? super CallContext> outsidePartition() {
      return receiver(klassIn(getInternalName(LoopOutsidePartition.class)));
   }

   public Matcher<? super CallContext> insidePartition() {
      return receiver(klassIn(getInternalName(LoopInsidePartition.class)));
   }

   private CombinableMatcher<JState> loopCondition() {
      return loopMethod(9);
   }

   private CombinableMatcher<JState> loopBody() {
      return loopMethod(10);
   }

   private CombinableMatcher<JState> loopExit() {
      return loopMethod(12);
   }

   private CombinableMatcher<JState> loopMethod(final int line) {
      return both(loopMethod).and(lineNumber(line));
   }

   final Matcher<JState> insideConstructorEntry = currentMethodIs(defaultConstructor(LoopInsidePartition.class));
   final CombinableMatcher<JState> insideConstructorExit = both(insideConstructorEntry).and(returnVoid());
   final Matcher<JState> mathMinMethod = currentMethodIs(method(Math.class, "min", "(II)I"));
   final Matcher<JState> insideMethodEntry = currentMethodIs(method(LoopInsidePartition.class, "method", "(I)V"));
   final Matcher<JState> insideMethodExit = both(insideMethodEntry).and(returnVoid());
   final Matcher<JState> loopMethod = currentMethodIs(method(LoopOutsidePartition.class, "entry", "(I)V"));

   @Test public void mismatchIsFound() {
      context.checking(new Expectations(){{
         oneOf(searchObserver).picked(with(entryPoint()), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideConstructorEntry)); inSequence(searchSequence);
         oneOf(searchObserver).picked(with(entryPoint()), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideConstructorEntry)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(insideConstructorEntry), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideConstructorExit)); inSequence(searchSequence);
         oneOf(searchObserver).picked(with(insideConstructorEntry), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideConstructorExit)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(insideConstructorExit), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).forkAt(with(mathMinMethod)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(insideConstructorExit), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).forkAt(with(mathMinMethod)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(mathMinMethod).and(pcIs(vm.feasbilityChecker(), icmpgt(symbol, 1)))), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodEntry)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(mathMinMethod).and(pcIs(vm.feasbilityChecker(), icmpgt(symbol, 2)))), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodEntry)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(mathMinMethod).and(pcIs(vm.feasbilityChecker(), invert(icmpgt(symbol, 1))))), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).forkAt(with(loopCondition())); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(mathMinMethod).and(pcIs(vm.feasbilityChecker(), invert(icmpgt(symbol, 2))))), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).forkAt(with(loopCondition())); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(insideMethodEntry).and(pcIs(vm.feasbilityChecker(), icmpgt(symbol, 1)))), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodExit)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(insideMethodEntry).and(pcIs(vm.feasbilityChecker(), icmpgt(symbol, 2)))), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodExit)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(
               both(loopExit()).
               and(pcIs(vm.feasbilityChecker(), 
                     and(invert(icmpgt(symbol, 1)),
                           invert(icmplt(0, symbol)))))), with(PSIDE)); inSequence(searchSequence);
                           oneOf(searchObserver).leaf(with(terminate())); inSequence(searchSequence);
                           
         oneOf(searchObserver).picked(with(
               both(loopExit()).
               and(pcIs(vm.feasbilityChecker(), 
                     and(invert(icmpgt(symbol, 2)),
                           invert(icmplt(0, symbol)))))), with(QSIDE)); inSequence(searchSequence);
                           oneOf(searchObserver).leaf(with(terminate())); inSequence(searchSequence);
                           
         oneOf(searchObserver).picked(with(
               both(loopBody()).
               and(pcIs(vm.feasbilityChecker(),
                     and(invert(icmpgt(symbol, 1)),
                         icmplt(0, symbol))))), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodEntry)); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(
               both(loopBody()).
               and(pcIs(vm.feasbilityChecker(), 
                     and(invert(icmpgt(symbol, 2)),
                         icmplt(0, symbol))))), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodEntry)); inSequence(searchSequence);



         oneOf(searchObserver).picked(with(both(insideMethodExit).and(pcIs(vm.feasbilityChecker(), icmpgt(symbol, 1)))), with(PSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).leaf(with(terminate())); inSequence(searchSequence);

         oneOf(searchObserver).picked(with(both(insideMethodExit).and(pcIs(vm.feasbilityChecker(), icmpgt(symbol, 2)))), with(QSIDE)); inSequence(searchSequence);
         oneOf(searchObserver).goal(with(insideMethodEntry)); inSequence(searchSequence);
      }});

      try {
         vm.execute(symbol);
         fail("should have been unbounded");
      } catch(final PartitionViolationException e) {
         assertThat(e.violationModel().intModelForBv32Expr(symbol), greaterThanOrEqualTo(2));
      }
   }
}
