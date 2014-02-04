package com.lexicalscope.svm.j.instruction.instrumentation;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource.InstructionSink;

public interface Instrumentation {
   void before(InstructionSink sink);
   void after(InstructionSink sink);
}
