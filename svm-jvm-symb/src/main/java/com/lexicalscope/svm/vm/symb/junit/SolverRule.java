package com.lexicalscope.svm.vm.symb.junit;

import static com.lexicalscope.fluentreflection.FluentReflection.object;
import static com.lexicalscope.fluentreflection.ReflectionMatchers.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.lexicalscope.fluentreflection.FluentField;
import com.lexicalscope.fluentreflection.FluentObject;
import com.lexicalscope.svm.j.instruction.symbolic.SymbInstructionFactory;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.Symbol;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SolverRule implements MethodRule {
   private final SymbInstructionFactory symbInstructionFactory;
   private final FeasibilityChecker feasibilityChecker;

   public SolverRule() {
      this(new FeasibilityChecker());
   }

   public SolverRule(
         final SymbInstructionFactory symbInstructionFactory,
         final FeasibilityChecker feasibilityChecker) {
      this.symbInstructionFactory = symbInstructionFactory;
      this.feasibilityChecker = feasibilityChecker;
   }

   public SolverRule(final FeasibilityChecker feasibilityChecker) {
      this(new SymbInstructionFactory(feasibilityChecker), feasibilityChecker);
   }

   public void createSymbols(final FluentObject<Object> object) {
      for (final FluentField field : object.fields(annotatedWith(Fresh.class))) {
         // should be field.matches(isType(ISymbol.class))
         // or field.isType(ISymbol.class)
         if(field.type().isType(reflectingOn(ISymbol.class))) {
            field.call(symbInstructionFactory.isymbol());
         } else if(field.type().isType(reflectingOn(IArrayAndLengthSymbols.class))) {
            field.call(
                  new IArrayAndLengthSymbols(
                        symbInstructionFactory.iasymbol(),
                        symbInstructionFactory.isymbol()));
         } else {
            throw new UnsupportedOperationException();
         }
      }
   }

   public boolean equivalent(final Symbol left, final Symbol right) {
      return feasibilityChecker.equivalent(left, right);
   }

   public Matcher<? super BoolSymbol> equivalent(final BoolSymbol expected) {
      return new TypeSafeDiagnosingMatcher<BoolSymbol>() {
         @Override public void describeTo(final Description description) {
            description.appendText("symbol equivalent to ").appendValue(expected);
         }

         @Override protected boolean matchesSafely(final BoolSymbol actual, final Description mismatchDescription) {
            mismatchDescription.appendText("symbol equivalent to ").appendValue(actual);
            return equivalent(expected, actual);
         }
      };
   }

   @Override public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
      return new Statement() {
         @Override public void evaluate() throws Throwable {
            createSymbols(object(target));
            base.evaluate();
         }};
   }

   public FeasibilityChecker checker() {
      return feasibilityChecker;
   }
}
