package com.lexicalscope.svm.j.instruction.concrete.nativ3;

import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.Vop;

public class NanoTimeOp implements Vop {
   @Override public void eval(final State ctx) {
      ctx.pushDoubleWord(System.nanoTime());
   }

   @Override public String toString() {
      return "NANOTIME";
   }
}