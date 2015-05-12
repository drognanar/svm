package com.lexicalscope.svm.search;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class SideMatchers {
   public static Matcher<GuidedSearchState> pSide() {
      return new TypeSafeDiagnosingMatcher<GuidedSearchState>() {
         @Override public void describeTo(final Description description) {
            description.appendText("p side");
         }

         @Override protected boolean matchesSafely(final GuidedSearchState item, final Description mismatchDescription) {
            final boolean result = item instanceof GuidedSearchSearchingP;
            mismatchDescription.appendValue(item.getClass().getSimpleName());
            return result;
         }
      };
   }

   public static Matcher<GuidedSearchState> qSide() {
      return new TypeSafeDiagnosingMatcher<GuidedSearchState>() {
         @Override public void describeTo(final Description description) {
            description.appendText("q side");
         }

         @Override protected boolean matchesSafely(final GuidedSearchState item, final Description mismatchDescription) {
            final boolean result = item instanceof GuidedSearchSearchingQ;
            mismatchDescription.appendValue(item.getClass().getSimpleName());
            return result;
         }
      };
   }
}
