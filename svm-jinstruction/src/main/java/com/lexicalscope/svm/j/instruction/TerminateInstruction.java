package com.lexicalscope.svm.j.instruction;

import static com.lexicalscope.svm.vm.j.InstructionCode.methodexit;

import java.util.Collection;
import java.util.Iterator;

import com.lexicalscope.svm.vm.TerminationException;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.InstructionCode;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.State;

public class TerminateInstruction implements Instruction {
   private Instruction prev;

   @Override public void eval(final State ctx) {
      // TODO[tim]: demeter
      throw new TerminationException();
   }

   @Override public Collection<Instruction> targetOf() {
      throw new IllegalStateException("TERMINATE may not be the target of JMPs");
   }

   @Override public void targetOf(final Instruction instruction) {
      throw new IllegalStateException("TERMINATE may not be the target of JMPs");
   }

   @Override public Instruction append(final Instruction instruction) {
      throw new IllegalStateException("TERMINATE has no successor");
   }

   @Override public void insertNext(final Instruction nodeE) {
      throw new IllegalStateException("TERMINATE has no successor");
   }

   @Override public void jmpTarget(final Instruction instruction) {
      throw new UnsupportedOperationException();
   }

   @Override public boolean hasNext() {
		return false;
   }

   @Override public Instruction next() {
      throw new IllegalStateException("TERMINATE has no successor");
   }

   @Override public Instruction jmpTarget() {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean equals(final Object obj) {
      return obj != null && obj.getClass().equals(this.getClass());
   }

   @Override
   public int hashCode() {
      return this.getClass().hashCode();
   }

   @Override
   public String toString() {
      return "TERMINATE";
   }

   @Override public InstructionCode code() {
      return methodexit;
   }

   @Override public void prevIs(final Instruction instruction) {
      this.prev = instruction;
   }

   @Override public Instruction prev() {
      return prev;
   }

   @Override public void insertHere(final Instruction node) {
      if(prev != null) {
         prev.insertNext(node);
      } else {
         node.append(this);
      }
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.methodexit();
   }

   @Override public Iterator<Instruction> iterator() {
      return new Iterator<Instruction>() {
         boolean exhausted;

         @Override public boolean hasNext() {
            return !exhausted;
         }

         @Override public Instruction next() {
            exhausted = true;
            return TerminateInstruction.this;
         }

         @Override public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
