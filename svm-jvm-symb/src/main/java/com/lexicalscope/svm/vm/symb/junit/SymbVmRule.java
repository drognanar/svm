package com.lexicalscope.svm.vm.symb.junit;

import com.lexicalscope.fluentreflection.FluentObject;
import com.lexicalscope.svm.j.instruction.symbolic.SymbInstructionFactory;
import com.lexicalscope.svm.vm.conc.JvmBuilder;
import com.lexicalscope.svm.vm.conc.StateSearchFactory;
import com.lexicalscope.svm.vm.conc.junit.VmRule;
import com.lexicalscope.svm.vm.symb.SymbVmFactory;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SymbVmRule extends VmRule {
   private final FeasibilityChecker feasibilityChecker;
   private final SolverRule solverRule;

   public SymbVmRule(final Class<?> ... loadFromWhereverTheseWereLoaded) {
      this();
      loadFrom(loadFromWhereverTheseWereLoaded);
   }

   public SymbVmRule() {
      this(new FeasibilityChecker(), null);
   }

   public SymbVmRule(final FeasibilityChecker feasibilityChecker, StateSearchFactory factory) {
      this(new SymbInstructionFactory(feasibilityChecker), feasibilityChecker, factory);
   }

   private SymbVmRule(final SymbInstructionFactory symbInstructionFactory, final FeasibilityChecker feasibilityChecker, StateSearchFactory factory) {
      super(getSearchFactory(symbInstructionFactory, feasibilityChecker, factory));
      this.feasibilityChecker = feasibilityChecker;
      this.solverRule = new SolverRule(symbInstructionFactory, feasibilityChecker);
   }

   private static JvmBuilder getSearchFactory(SymbInstructionFactory symbInstructionFactory, FeasibilityChecker feasibilityChecker, StateSearchFactory factory) {
      return factory == null
              ? SymbVmFactory.symbolicVmBuilder(symbInstructionFactory, feasibilityChecker)
              : SymbVmFactory.symbolicVmBuilder(symbInstructionFactory, factory);
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
