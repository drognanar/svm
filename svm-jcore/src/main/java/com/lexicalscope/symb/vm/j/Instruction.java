package com.lexicalscope.symb.vm.j;

import java.util.Collection;


/**
 * double-linked intra-procedural graph of instructions.
 *
 * @author tim
 */
public interface Instruction {
   void eval(State ctx);

   Instruction append(Instruction instruction);
   void insertNext(Instruction nodeE);
   void jmpTarget(Instruction instruction);
   void targetOf(Instruction instruction);
   void prevIs(Instruction instruction);

   /**
    * Inserts a node here and shifts the rest of the nodes down.
    * Fixes up any jmp instructions to target the inserted node.
    */
   void insertHere(Instruction nodeE);

   boolean hasNext();
   Instruction next();
   Instruction prev();
   Instruction jmpTarget();
   Collection<Instruction> targetOf();

   InstructionCode code();
}
