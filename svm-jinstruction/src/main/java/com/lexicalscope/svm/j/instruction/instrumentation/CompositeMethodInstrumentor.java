package com.lexicalscope.svm.j.instruction.instrumentation;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class CompositeMethodInstrumentor implements MethodInstrumentor {
   private final InstructionFinder finder;
   private final InstructionInstrumentor instrumentor;

   public CompositeMethodInstrumentor(final InstructionFinder finder, final InstructionInstrumentor instrumentor) {
      this.finder = finder;
      this.instrumentor = instrumentor;
   }

   @Override public Instruction instrument(final InstructionSource instructions, final SMethodDescriptor method, final Instruction methodEntry) {
      final InstructionCollector collectedInstructions = new InstructionCollector();
      finder.findInstruction(methodEntry, collectedInstructions);
      for (final Instruction instruction : collectedInstructions) {
         instrumentor.candidate(instruction);
      }
      return methodEntry;
   }
}
