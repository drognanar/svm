package com.lexicalscope.svm.vm.j;

import java.util.Collection;


/**
 * double-linked intra-procedural graph of instructions.
 *
 * @author tim
 */
public interface Instruction extends Iterable<Instruction> {
   void eval(JState ctx);

   Instruction append(Instruction instruction);
   void insertNext(Instruction instruction);
   void jmpTarget(Instruction instruction);
   void targetOf(Instruction instruction);
   void prevIs(Instruction instruction);
   void nextIs(Instruction instruction);

   Vop op();
   void replaceOp(Vop op);

   /**
    * Inserts a node here and shifts the rest of the nodes down.
    * Fixes up any jmp instructions to target the inserted node.
    */
   void insertHere(Instruction instruction);

   /**
    * Replace this node.
    * Fixes up any jmp instructions to target the inserted node.
    */
   void replaceWith(Instruction instruction);

   boolean hasNext();
   Instruction next();
   Instruction prev();
   Instruction jmpTarget();
   Collection<Instruction> targetOf();

   InstructionCode code();
   int line();

   <T> T query(InstructionQuery<T> instructionQuery);
}
