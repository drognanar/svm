package com.lexicalscope.svm.search2;



public interface TraceTreeObserver {
   void pstateAvailable(TraceTree traceTree);
   void qstateAvailable(TraceTree traceTree);
   void qstateUnavailable(TraceTree traceTree);
   void pstateUnavailable(TraceTree traceTree);
}
