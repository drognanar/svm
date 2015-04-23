package com.lexicalscope.svm.partition.trace.symb;

import java.util.Iterator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.Symbol;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.TraceBuilder;
import com.lexicalscope.svm.partition.trace.TraceElement;
import com.lexicalscope.svm.vm.symb.junit.SolverRule;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class SymbolicTraceMatchers {
   public static Matcher<TraceBuilder> equivalentTo(final SolverRule solver, final TraceBuilder expected) {
      final Matcher<Trace> traceMatcher = equivalentTo(solver, expected.build());
      return new TypeSafeDiagnosingMatcher<TraceBuilder>() {
         @Override public void describeTo(final Description description) {
            description.appendDescriptionOf(traceMatcher);
         }

         @Override protected boolean matchesSafely(final TraceBuilder item, final Description mismatchDescription) {
            final Trace actualTrace = item.build();
            final boolean matches = traceMatcher.matches(actualTrace);
            if(!matches) {
               traceMatcher.describeMismatch(actualTrace, mismatchDescription);
            }
            return matches;
         }
      };
   }

   private static Matcher<Trace> equivalentTo(final SolverRule solver, final Trace expectedTrace) {
      return equivalentTo(solver.checker(), expectedTrace);
   }

   public static Matcher<TraceElement> traceElementMatcher(final FeasibilityChecker checker, final TraceElement expectedTraceElement) {
      return new TypeSafeDiagnosingMatcher<TraceElement>() {
         @Override
         protected boolean matchesSafely(TraceElement actualTraceElement, Description mismatchDescription) {
            if(expectedTraceElement.methodName().equals(actualTraceElement.methodName()) &&
                    expectedTraceElement.isCall() == actualTraceElement.isCall()) {

               final Object[] actualArgs = actualTraceElement.args();
               final Object[] expectedArgs = expectedTraceElement.args();
               if(expectedArgs.length != actualArgs.length) {
                  mismatchDescription
                          .appendText("argument length mismatch ")
                          .appendValue(expectedTraceElement)
                          .appendText(" and ")
                          .appendValue(actualTraceElement);
                  return false;
               }
               for (int i = 0; i < expectedArgs.length; i++) {
                  final Object actualArg = actualArgs[i];
                  final Object expectedArg = expectedArgs[i];
                  if(   actualArg instanceof Symbol
                          && expectedArg instanceof Symbol) {
                     if(!checker.equivalent((Symbol) expectedArg, (Symbol) actualArg))
                     {
                        mismatchDescription.appendText("unable to prove equivalence of ")
                                .appendValue(expectedArg)
                                .appendText(" and ")
                                .appendValue(actualArg);
                        return false;
                     }
                  } else if (!actualArg.equals(expectedArg)) {
                     mismatchDescription.appendText("argument mismatch of ")
                             .appendValue(expectedArg.getClass())
                             .appendText(":")
                             .appendValue(expectedArg)
                             .appendText(" and ")
                             .appendValue(actualArg.getClass())
                             .appendText(":")
                             .appendValue(actualArg);
                     return false;
                  }
               }

            } else {
               mismatchDescription.appendText("expected ").appendValue(expectedTraceElement);
               mismatchDescription.appendText("actual ").appendValue(actualTraceElement);
               return false;
            }
            return true;
         }

         @Override
         public void describeTo(Description description) {
            description.appendText("trace item equivalent to ").appendValue(expectedTraceElement);
         }
      };
   }

   public static Matcher<Trace> equivalentTo(final FeasibilityChecker checker, final Trace expectedTrace) {
      return new TypeSafeDiagnosingMatcher<Trace>() {
         @Override public void describeTo(final Description description) {
            description.appendText("trace equivalent to ").appendValue(expectedTrace);
         }

         @Override protected boolean matchesSafely(final Trace actualTrace, final Description mismatchDescription) {
            final Iterator<TraceElement> expectedIterator = expectedTrace.iterator();
            final Iterator<TraceElement> actualIterator = actualTrace.iterator();
            while(expectedIterator.hasNext() && actualIterator.hasNext()) {
               final TraceElement expectedTraceElement = expectedIterator.next();
               final TraceElement actualTraceElement = actualIterator.next();


               if (!traceElementMatcher(checker, expectedTraceElement).matches(actualTraceElement))
                  return false;
            }
            // when comparing traces we truncate the longest trace to the
            // length of the shortest trace

            // TODO[tim]: take into account program termination in the trace
            // Take into account the possibility that traces are of different length.
            return !expectedIterator.hasNext() && !actualIterator.hasNext();
         }
      };
   }

   public static Matcher<Trace> equivalentTo(final SymbVmRule vmRule, final Trace trace) {
      return equivalentTo(vmRule.solver(), trace);
   }
}
