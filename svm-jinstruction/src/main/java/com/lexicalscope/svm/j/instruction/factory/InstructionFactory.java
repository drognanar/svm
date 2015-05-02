package com.lexicalscope.svm.j.instruction.factory;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import com.lexicalscope.svm.j.instruction.concrete.ops.Binary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.BinaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.Nullary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.NullaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.UnaryOperator;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.Vop;

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
   Vop reflectionNewArray();
   Vop aaStore();
   Vop iaStore();

   Vop iaLoad();
   Vop aaLoad();

   Op<?> newObject(KlassInternalName klassDesc);
   Vop putField(FieldInsnNode fieldInsnNode);
   Vop getField(FieldInsnNode fieldInsnNode);

   Vop ifge(JumpInsnNode jumpInsnNode);
   Vop ifgt(JumpInsnNode jumpInsnNode);
   Vop ifle(JumpInsnNode jumpInsnNode);
   Vop iflt(JumpInsnNode jumpInsnNode);
   Vop ifeq(JumpInsnNode jumpInsnNode);
   Vop ifne(JumpInsnNode jumpInsnNode);
   Vop ifnull(JumpInsnNode jumpInsnNode);
   Vop ifnonnull(JumpInsnNode jumpInsnNode);

   Vop ificmpeq(JumpInsnNode jumpInsnNode);
   Vop ificmpne(JumpInsnNode jumpInsnNode);
   Vop ificmple(JumpInsnNode jumpInsnNode);
   Vop ificmpge(JumpInsnNode jumpInsnNode);
   Vop ificmplt(JumpInsnNode jumpInsnNode);
   Vop ificmpgt(JumpInsnNode jumpInsnNode);

   Vop ifacmpeq(JumpInsnNode jumpInsnNode);
   Vop ifacmpne(JumpInsnNode jumpInsnNode);

   Vop loadArg(Object object, InstructionSource instructions);

   // initial values for fields
   Object initInt();
}
