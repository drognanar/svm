package com.lexicalscope.svm.examples.loop.broken;

import static java.lang.Math.min;


public class LoopOutsidePartition {
   public void entry(final int input) {
      final LoopInsidePartition inside = new LoopInsidePartition();
      for (int i = 0; i < min(input, 1); i++) {
         inside.method(i);
      }
   }

   public static void entryPoint(final int input) {
      new LoopOutsidePartition().entry(input);
   }
}
