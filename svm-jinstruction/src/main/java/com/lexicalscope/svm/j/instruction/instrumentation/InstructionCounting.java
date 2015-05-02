package com.lexicalscope.svm.j.instruction.instrumentation;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.svm.vm.j.Instruction;

public class InstructionCounting implements InstructionInstrumentor {
   private int count;

   @Override public void candidate(final Instruction instruction) {
      count++;
   }

   public int count() {
      return count;
   }

   public static Matcher<? super InstructionCounting> countIs(final int expected) {
      return new TypeSafeDiagnosingMatcher<InstructionCounting>() {
         @Override public void describeTo(final Description description) {
            description.appendValue(expected).appendText(" matched instructions");
         }

         @Override protected boolean matchesSafely(
               final InstructionCounting item,
               final Description mismatchDescription) {
            mismatchDescription.appendValue(item.count()).appendText(" matched instructions");
            return item.count() == expected;
         }
      };
   }
}
