package com.lexicalscope.symb.vm.instructions;

import static com.google.common.collect.Lists.reverse;

import java.util.ArrayList;
import java.util.List;

import com.lexicalscope.symb.vm.Instruction;
import com.lexicalscope.symb.vm.InstructionInternalNode;
import com.lexicalscope.symb.vm.classloader.MethodBody;

public class StatementBuilder {
   private final List<Instruction> instructions = new ArrayList<>();
   private int maxStack;
   private int maxLocals;
   private final BaseInstructions baseInstructions;

   public StatementBuilder(final BaseInstructions baseInstructions) {
      this.baseInstructions = baseInstructions;
   }

   public StatementBuilder maxLocals(final int maxLocals) {
      this.maxLocals = maxLocals;
      return this;
   }

   public StatementBuilder maxStack(final int maxStack) {
      this.maxStack = maxStack;
      return this;
   }

   public StatementBuilder aconst_null() {
      instructions.add(baseInstructions.aconst_null());
      return this;
   }

   public StatementBuilder return1() {
      instructions.add(baseInstructions.return1());
      return this;
   }

   public StatementBuilder returnVoid() {
      instructions.add(baseInstructions.returnVoid());
      return this;
   }

   public MethodBody build() {
      InstructionInternalNode next = null;
      for (final Instruction instruction : reverse(instructions)) {
         final InstructionInternalNode node = new InstructionInternalNode(instruction);
         if(next != null) {node.next(next);}
         next = node;
      }
      return new MethodBody(next, maxStack, maxLocals);
   }
}
