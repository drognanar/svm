package com.lexicalscope.svm.j.instruction.instrumentation;

import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class NullInstrumentation implements Instrumentation {
   @Override public Instruction instrument(final SMethodDescriptor method, final Instruction methodEntry) {
      return methodEntry;
   }
}
