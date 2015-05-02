package com.lexicalscope.svm.j.instruction.instrumentation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.lexicalscope.svm.vm.j.Instruction;

final class InstructionCollector implements InstructionInstrumentor, Iterable<Instruction> {
   private final Set<Instruction> toInstrument = new HashSet<Instruction>();

   @Override public void candidate(final Instruction instruction) {
      toInstrument.add(instruction);
   }

   @Override public Iterator<Instruction> iterator() {
      return toInstrument.iterator();
   }
}