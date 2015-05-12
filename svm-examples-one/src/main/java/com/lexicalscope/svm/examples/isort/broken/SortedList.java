package com.lexicalscope.svm.examples.isort.broken;

import java.util.ArrayList;

public class SortedList {
   private final ArrayList<Integer> internal = new ArrayList<>();
   //private final LinkedList<Integer> internal = new LinkedList<>();

   public void add(final int newElement) {
      for (int j = 0; j < internal.size(); j++) {
         if(internal.get(j) < newElement) {
            internal.add(j, newElement);
            return;
         }
      }
      internal.add(newElement);
   }

   public int size() {
      return internal.size();
   }

   public int get(final int i) {
      return internal.get(i);
   }
}
