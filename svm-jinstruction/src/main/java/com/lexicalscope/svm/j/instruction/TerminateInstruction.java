package com.lexicalscope.svm.j.instruction;

import static com.lexicalscope.svm.vm.j.InstructionCode.methodexit;

import java.util.Collection;
import java.util.Iterator;

import com.lexicalscope.svm.metastate.HashMetaState;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.vm.TerminationException;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.InstructionCode;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

public class TerminateInstruction implements Instruction {
   private final HashMetaState meta = new HashMetaState();

   private Instruction prev;

   @Override public void eval(final JState ctx) {
      // TODO[tim]: demeter
      ctx.setMeta(TerminationMetaKey.TERMINATION, true);
      ctx.terminate();
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

   @Override public void replaceWith(final Instruction instruction) {
      throw new IllegalStateException("TERMINATE cannot be replaced");
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

   @Override public void nextIs(final Instruction instruction) {
      throw new IllegalStateException("TERMINATE has no successor");
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

   @Override public Vop op() {
      throw new IllegalStateException("TERMINATE has no operation");
   }

   @Override public void replaceOp(final Vop op) {
      throw new IllegalStateException("TERMINATE has no operation");
   }

   @Override public int line() {
      return -1;
   }

   @Override public <T> T get(final MetaKey<T> key) {
      return meta.get(key);
   }

   @Override public <T> void set(final MetaKey<T> key, final T value) {
      meta.set(key, value);
   }

   @Override public boolean contains(final MetaKey<?> key) {
      return meta.contains(key);
   }

   @Override public void remove(final MetaKey<?> key) {
      meta.remove(key);
   }
}
