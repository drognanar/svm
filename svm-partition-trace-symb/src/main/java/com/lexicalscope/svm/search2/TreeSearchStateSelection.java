package com.lexicalscope.svm.search2;

import java.util.List;

public interface TreeSearchStateSelection {
   StatesCollection statesCollection(TraceTreeSideObserver ttObserver);

   TraceTree qnode(List<TraceTree> qstatesAvailable);
   TraceTree pnode(List<TraceTree> pstatesAvailable);
}
