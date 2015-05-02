package com.lexicalscope.svm.j.instruction.instrumentation;

import com.lexicalscope.svm.vm.j.Instruction;

public interface InstructionFinder {
   void findInstruction(Instruction methodEntry, InstructionInstrumentor instrumentor);
}
