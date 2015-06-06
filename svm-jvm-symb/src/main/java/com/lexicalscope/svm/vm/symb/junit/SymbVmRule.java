package com.lexicalscope.svm.vm.symb.junit;

import com.lexicalscope.fluentreflection.FluentObject;
import com.lexicalscope.svm.j.instruction.symbolic.SymbInstructionFactory;
import com.lexicalscope.svm.vm.conc.DepthFirstStateSearchFactory;
import com.lexicalscope.svm.vm.conc.JvmBuilder;
import com.lexicalscope.svm.vm.conc.StateSearchFactory;
import com.lexicalscope.svm.vm.conc.junit.VmRule;
import com.lexicalscope.svm.vm.symb.SymbVmFactory;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SymbVmRule extends VmRule {
   private final FeasibilityChecker feasibilityChecker;
   private final SolverRule solverRule;

   private SymbVmRule(final SymbInstructionFactory symbInstructionFactory, final FeasibilityChecker feasibilityChecker, final JvmBuilder factory) {
      super(factory);
      this.feasibilityChecker = feasibilityChecker;
      this.solverRule = new SolverRule(symbInstructionFactory, feasibilityChecker);
   }

   public static SymbVmRule createSymbVmRuleLoadingFrom(final Class<?>... loadFromWhereverTheseWereLoaded) {
      final SymbVmRule result = createSymbVmRule();
      result.loadFrom(loadFromWhereverTheseWereLoaded);
      return result;
   }

   public static SymbVmRule createSymbVmRule() {
      return createSymbVmRule(new FeasibilityChecker(), new DepthFirstStateSearchFactory());
   }

   public static SymbVmRule createSymbVmRule(final FeasibilityChecker feasibilityChecker, final StateSearchFactory factory) {
      return createSymbVmRule(new SymbInstructionFactory(feasibilityChecker), feasibilityChecker, factory);
   }

   public static SymbVmRule createSymbVmRule(final SymbInstructionFactory symbInstructionFactory, final FeasibilityChecker feasibilityChecker, final StateSearchFactory factory) {
      return new SymbVmRule(symbInstructionFactory, feasibilityChecker, SymbVmFactory.symbolicVmBuilder(symbInstructionFactory, factory));
   }

   @Override protected void configureTarget(final FluentObject<Object> object) {
      solverRule.createSymbols(object);
   }

   @Override protected void cleanup() {
      feasibilityChecker.close();
   }

   public FeasibilityChecker feasbilityChecker() {
      return feasibilityChecker;
   }

   public SolverRule solver() {
      return solverRule;
   }
}
