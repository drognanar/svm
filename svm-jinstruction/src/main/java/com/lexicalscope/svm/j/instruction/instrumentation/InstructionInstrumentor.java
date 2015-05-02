package com.lexicalscope.svm.j.instruction.instrumentation;

import com.lexicalscope.svm.vm.j.Instruction;

public interface InstructionInstrumentor {
   void candidate(Instruction instruction);
}
