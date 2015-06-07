package com.lexicalscope.svm.j.instruction.instrumentation.finders;

import static org.hamcrest.Matchers.any;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.instrumentation.InstructionFinder;
import com.lexicalscope.svm.j.instruction.instrumentation.InstructionInstrumentor;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.queries.IsConstructorCall;
import com.lexicalscope.svm.vm.j.queries.IsNewInstruction;

import java.util.Stack;

public class FindConstructorCall implements InstructionFinder {
   private interface SearchState {
      void matchInstruction(Instruction instruction, InstructionInstrumentor instrumentor);
   }

   private class LookingForNew implements FindConstructorCall.SearchState {
      @Override public void matchInstruction(final Instruction instruction, final InstructionInstrumentor instrumentor) {
         if(instruction.query(new IsNewInstruction(klass)))
         {
            newCalls++;
            state = new LookingForConstructor();
         }
      }
   }

   private class LookingForConstructor implements FindConstructorCall.SearchState {
      @Override public void matchInstruction(final Instruction instruction, final InstructionInstrumentor instrumentor) {
         if (instruction.query(new IsNewInstruction(klass))) {
            newCalls++;
         }
         else if(instruction.query(new IsConstructorCall(klass)))
         {
            newCalls--;
            instrumentor.candidate(instruction);
            if (newCalls == 0) {
               state = new LookingForNew();
            }
         }
         else if (instruction.query(new IsConstructorCall(any(KlassInternalName.class))))
         {
            newCalls--;
            assert newCalls > 0: "Top constructor needs to match the type of NEW";
         }
      }
   }

   private FindConstructorCall.SearchState state = new LookingForNew();
   private int newCalls = 0;
   private final Matcher<KlassInternalName> klass;

   public FindConstructorCall(final Matcher<KlassInternalName> klass) {
      this.klass = klass;
   }

   @Override public void findInstruction(final Instruction methodEntry, final InstructionInstrumentor instrumentor) {
      for (final Instruction instruction : methodEntry) {
         state.matchInstruction(instruction, instrumentor);
      }
      assert newCalls == 0: "Unbalanced NEW and init calls.";
   }
}