package com.lexicalscope.svm.search2;



public interface TraceTreeObserver {
   FastListLocation pstateAvailable(TraceTree traceTree);
   FastListLocation qstateAvailable(TraceTree traceTree);
   void qstateUnavailable(TraceTree traceTree);
   void pstateUnavailable(TraceTree traceTree);
}
