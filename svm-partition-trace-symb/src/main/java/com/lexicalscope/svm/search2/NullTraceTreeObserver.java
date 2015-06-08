package com.lexicalscope.svm.search2;



public class NullTraceTreeObserver implements TraceTreeObserver {
   @Override public FastListLocation pstateAvailable(final TraceTree traceTree) {
      return new FastListLocation(0);
   }

   @Override public FastListLocation qstateAvailable(final TraceTree traceTree) {
      return new FastListLocation(0);
   }

   @Override public void qstateUnavailable(final TraceTree tt) {

   }

   @Override public void pstateUnavailable(final TraceTree traceTree) {

   }
}
