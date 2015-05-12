package com.lexicalscope.svm.search2;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.svm.z3.FeasibilityChecker;

public class TraceTreeChildMismatchDetector {
   private final FeasibilityChecker checker;

   public TraceTreeChildMismatchDetector(final FeasibilityChecker checker) {
      this.checker = checker;
   }

   public boolean mismatch(final TraceTree tree, final MismatchReport mismatchReport) {
      final List<TraceTree> children = new ArrayList<>(tree.children());
      for (int i = 0; i < children.size(); i++) {
         for (int j = 0; j < children.size(); j++) {
            if(i != j) {
               final TraceTree childI = children.get(i);
               final TraceTree childJ = children.get(j);

               if (checker.overlap(childI.pPc(), childJ.qPc()))
               {
                  mismatchReport.mismatch(checker, childI.nodeTrace(), childI.pPc(), childJ.nodeTrace(), childJ.qPc());
                  return true;
               }
               if(checker.overlap(childI.qPc(), childJ.pPc()))
               {
                  mismatchReport.mismatch(checker, childJ.nodeTrace(), childJ.pPc(), childI.nodeTrace(), childI.qPc());
                  return true;
               }
            }
         }
      }
      return false;
   }

   public static Matcher<TraceTree> hasMismatch(final TraceTreeChildMismatchDetector mismatchDetector) {
      return new TypeSafeDiagnosingMatcher<TraceTree>() {
         @Override public void describeTo(final Description description) {
            description.appendText("a trace tree with a trace mismatch between its children");
         }

         @Override protected boolean matchesSafely(final TraceTree item, final Description mismatchDescription) {
            return mismatchDetector.mismatch(item, new NullMismatchReport());
         }
      };
   }
}