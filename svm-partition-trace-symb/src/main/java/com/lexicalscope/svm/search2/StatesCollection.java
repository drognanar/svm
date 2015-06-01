package com.lexicalscope.svm.search2;

import com.lexicalscope.svm.vm.j.JState;

public interface StatesCollection extends Iterable<JState> {
   void add(JState state);

   JState pickState();
}
