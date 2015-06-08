package com.lexicalscope.svm.vm;

public class StateCountSearchLimit implements SearchLimits {
   private int count = 0;

   @Override public boolean withinLimits() {
      return count < 50000;
   }

   @Override public void searchedState() {
      count++;
   }

   @Override public void reset() {
      count = 0;
   }

   @Override public void done() {
   }
}
