package com.lexicalscope.svm.j.instruction.concrete.l0ng;

import com.lexicalscope.symb.vm.j.State;
import com.lexicalscope.symb.vm.j.Vop;

public class L2IOp implements Vop {
   @Override public void eval(final State ctx) {
      ctx.push((int)(long)ctx.popDoubleWord());
   }

   @Override public String toString() {
      return "L2I";
   }
}
