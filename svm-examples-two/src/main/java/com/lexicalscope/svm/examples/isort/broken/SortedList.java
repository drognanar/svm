package com.lexicalscope.svm.examples.isort.broken;

import java.util.LinkedList;

public class SortedList {
   private final LinkedList<Integer> internal = new LinkedList<>();

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

/*
P Path condition ((AND (AND (AND (AND (AND (< 0 #i1) (< 1 #i1)) (>= (select #ia0 0) (select #ia0 1))) (< 2 #i1)) (! (>= (select #ia0 0) (select #ia0 2)))) (! (< 3 #i1)))) reached trace Trace of 20 elements
Q Path condition ((AND (AND (AND (AND (AND (! (>= 0 #i1)) (! (>= 1 #i1))) (>= (select #ia0 0) (select #ia0 1))) (! (>= 2 #i1))) (! (>= (select #ia0 0) (select #ia0 2)))) (>= 3 #i1))) reached trace Trace of 20 elements

Looks like the array list version screws it up, why?

[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.<init>()V - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.<init>()V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - (Alias-0, (select #ia0 0))
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - (Alias-0, (select #ia0 1))
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - (Alias-0, (select #ia0 2))
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (3)
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - (Alias-0, 0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - ((select #ia0 2))
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (3)
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - (Alias-0, 1)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - ((select #ia0 0))
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (3)
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - (Alias-0, 2)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - ((select #ia0 0))


[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.<init>()V - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.<init>()V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - (Alias-0, (select #ia0 0))
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - (Alias-0, (select #ia0 1))
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - (Alias-0, (select #ia0 2))
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.add(I)V - ()
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (3)
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - (Alias-0, 0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - ((select #ia0 2))
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (3)
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - (Alias-0, 1)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - ((select #ia0 0))
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (Alias-0)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.size()I - (3)
[CALL]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - (Alias-0, 2)
[RETURN]com/lexicalscope/svm/examples/isort/broken/SortedList.get(I)I - ((select #ia0 1))
*/
