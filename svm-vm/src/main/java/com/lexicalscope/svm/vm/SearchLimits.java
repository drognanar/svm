package com.lexicalscope.svm.vm;

public interface SearchLimits {
   boolean withinLimits();

   void searchedState();
   void reset();
   void done();
}
