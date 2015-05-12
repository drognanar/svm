package com.lexicalscope.svm.search;

import com.lexicalscope.svm.search2.Side;
import com.lexicalscope.svm.vm.j.JState;

public interface GuidedSearchObserver {
   void picked(JState pending, GuidedSearchState side); // old one
   void picked(JState pending, Side currentSide); // new one

   void goal(JState pending);

   void leaf(JState pending);

   void forkAt(JState parent);
}
