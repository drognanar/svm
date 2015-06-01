package com.lexicalscope.svm.vm.symb.matchers;

import static com.lexicalscope.MatchersAdditional.after;
import static com.lexicalscope.svm.j.instruction.symbolic.PcMetaKey.PC;
import static com.lexicalscope.svm.vm.j.StateMatchers.metaIs;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.MatchersAdditional.TransformMatcherBuilder;
import com.lexicalscope.MemoizeTransform;
import com.lexicalscope.Transform;
import com.lexicalscope.svm.j.instruction.symbolic.PcMatchers;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.Symbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.SymbolMatchers;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SymbStateMatchers {

   public static class SimplifyingMatcherBuilder {
      private final FeasibilityChecker feasibilityChecker;

      public SimplifyingMatcherBuilder(final FeasibilityChecker feasibilityChecker) {
         this.feasibilityChecker = feasibilityChecker;
      }

      public Matcher<? super JState> toInt(final int expectedValue) {
         return after(SymbStateMatchers.stateToModel(feasibilityChecker)).matches(SymbolMatchers.symbolEquivalentTo(expectedValue));
      }
   }

   public static SimplifyingMatcherBuilder resultSimplifies(
         final FeasibilityChecker feasibilityChecker) {
      return new SimplifyingMatcherBuilder(feasibilityChecker);
   }

   public static Transform<Symbol,JState> stateToModel(final FeasibilityChecker feasibilityChecker) {
      return new MemoizeTransform<>(new ModelForStateTransform(feasibilityChecker));
   }

   public static TransformMatcherBuilder<Symbol, JState> toModel(final FeasibilityChecker feasibilityChecker) {
      return after(stateToModel(feasibilityChecker));
   }

   public static Matcher<JState> pcIs(
         final FeasibilityChecker checker, 
         final BoolSymbol expected) {
      return metaIs(PC, new TypeSafeDiagnosingMatcher<BoolSymbol>() {
         @Override public void describeTo(Description description) {
            description.appendValue("PC equivalent to ").appendValue(expected);
         }

         @Override protected boolean matchesSafely(BoolSymbol item, Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return checker.equivalent(expected, item);
         }
      });
   }
}
