package com.lexicalscope.svm.j.instruction.instrumentation;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public interface MethodInstrumentor {
   Instruction instrument(InstructionSource instructions, SMethodDescriptor method, Instruction methodEntry);
}
