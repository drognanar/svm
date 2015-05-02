package com.lexicalscope.svm.j.instruction.instrumentation.finders;

import static org.hamcrest.Matchers.any;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.instrumentation.InstructionFinder;
import com.lexicalscope.svm.j.instruction.instrumentation.InstructionInstrumentor;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.queries.IsConstructorCall;
import com.lexicalscope.svm.vm.j.queries.IsNewInstruction;

public class FindConstructorCall implements InstructionFinder {
   private interface SearchState {
      void matchInstruction(Instruction instruction, InstructionInstrumentor instrumentor);
   }

   private class LookingForNew implements FindConstructorCall.SearchState {
      @Override public void matchInstruction(final Instruction instruction, final InstructionInstrumentor instrumentor) {
         if(instruction.query(new IsNewInstruction(klass)))
         {
            state = new LookingForConstructor();
         }
      }
   }

   private class LookingForConstructor implements FindConstructorCall.SearchState {
      @Override public void matchInstruction(final Instruction instruction, final InstructionInstrumentor instrumentor) {
         assert !instruction.query(new IsNewInstruction(klass)) :
            "found two new instructions with no constructor inbetween";

         if(instruction.query(new IsConstructorCall(klass)))
         {
            instrumentor.candidate(instruction);
            state = new LookingForNew();
         }
         else
         {
            assert !instruction.query(new IsConstructorCall(any(KlassInternalName.class))) :
               "found the wrong constructor after a new instruction";
         }
      }
   }

   private FindConstructorCall.SearchState state = new LookingForNew();
   private final Matcher<KlassInternalName> klass;

   public FindConstructorCall(final Matcher<KlassInternalName> klass) {
      this.klass = klass;
   }

   @Override public void findInstruction(final Instruction methodEntry, final InstructionInstrumentor instrumentor) {
      for (final Instruction instruction : methodEntry) {
         state.matchInstruction(instruction, instrumentor);
      }
   }
}