package com.lexicalscope.symb.vm.instructions;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import com.lexicalscope.symb.vm.Instruction;
import com.lexicalscope.symb.vm.Snapshotable;
import com.lexicalscope.symb.vm.Vop;
import com.lexicalscope.symb.vm.instructions.ops.Binary2Operator;
import com.lexicalscope.symb.vm.instructions.ops.BinaryOperator;
import com.lexicalscope.symb.vm.instructions.ops.Nullary2Operator;
import com.lexicalscope.symb.vm.instructions.ops.NullaryOperator;
import com.lexicalscope.symb.vm.instructions.ops.UnaryOperator;

public interface InstructionFactory {
   BinaryOperator iaddOperation();
   BinaryOperator imulOperation();
   BinaryOperator isubOperation();
   BinaryOperator iandOperation();
   UnaryOperator inegOperation();

   Binary2Operator landOperation();

   BinaryOperator fmulOperation();
   BinaryOperator fdivOperation();
   BinaryOperator faddOperation();
   BinaryOperator fsubOperation();

   NullaryOperator iconst(int val);
   Nullary2Operator lconst(long val);
   NullaryOperator fconst(float val);
   Nullary2Operator dconst(double val);

   Vop newArray(Object initialFieldValue);
   Vop aNewArray();
   Vop aaStore();
   Vop iaStore();

   Vop iaLoad();
   Vop aaLoad();

   Vop putField(FieldInsnNode fieldInsnNode);
   Vop getField(FieldInsnNode fieldInsnNode);

   Instruction branchIfGe(JumpInsnNode jumpInsnNode);
   Instruction branchIfGt(JumpInsnNode jumpInsnNode);
   Instruction branchIfLe(JumpInsnNode jumpInsnNode);
   Instruction branchIfLt(JumpInsnNode jumpInsnNode);
   Instruction branchIfEq(JumpInsnNode jumpInsnNode);
   Instruction branchIfNe(JumpInsnNode jumpInsnNode);
   Instruction branchIfNull(JumpInsnNode jumpInsnNode);
   Instruction branchIfNonNull(JumpInsnNode jumpInsnNode);

   Instruction branchIfICmpEq(JumpInsnNode jumpInsnNode);
   Instruction branchIfICmpNe(JumpInsnNode jumpInsnNode);
   Instruction branchIfICmpLe(JumpInsnNode jumpInsnNode);
   Instruction branchIfICmpGe(JumpInsnNode jumpInsnNode);
   Instruction branchIfICmpLt(JumpInsnNode jumpInsnNode);
   Instruction branchIfICmpGt(JumpInsnNode jumpInsnNode);

   Instruction branchIfACmpEq(JumpInsnNode jumpInsnNode);
   Instruction branchIfACmpNe(JumpInsnNode jumpInsnNode);

   Instruction loadArg(Object object);

   Snapshotable<?> initialMeta();

   // initial values for fields
   Object initInt();
}