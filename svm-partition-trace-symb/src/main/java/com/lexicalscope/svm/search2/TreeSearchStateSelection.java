package com.lexicalscope.svm.search2;

public interface TreeSearchStateSelection {
   StatesCollection statesCollection(TraceTreeSideObserver ttObserver);

   TraceTree qnode(FastRemovalList<TraceTree> qstatesAvailable);
   TraceTree pnode(FastRemovalList<TraceTree> pstatesAvailable);
}
