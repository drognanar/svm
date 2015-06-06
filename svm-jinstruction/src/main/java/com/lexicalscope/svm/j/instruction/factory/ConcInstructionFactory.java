package com.lexicalscope.svm.j.instruction.factory;

import com.lexicalscope.svm.j.instruction.concrete.integer.IDivOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IRemOperator;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import com.lexicalscope.svm.j.instruction.LinearOp;
import com.lexicalscope.svm.j.instruction.concrete.LoadConstantArg;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayLoadOp;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayStoreOp;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.j.instruction.concrete.array.NewConcArray;
import com.lexicalscope.svm.j.instruction.concrete.branch.BranchInstruction;
import com.lexicalscope.svm.j.instruction.concrete.branch.BranchPredicate;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.ICmpBranchPredicate;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.ICmpOp;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifacmpeq;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifacmpne;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifeq;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifge;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifgt;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ificmpeq;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ificmpge;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ificmpgt;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ificmple;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ificmplt;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ificmpne;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifle;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Iflt;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifne;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifnonnull;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifnull;
import com.lexicalscope.svm.j.instruction.concrete.d0uble.DConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FAddOp;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FDivOp;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FMulOp;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FSubOp;
import com.lexicalscope.svm.j.instruction.concrete.integer.IAddOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IAndOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IMulOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.INegOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.ISubOperator;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.LAndOp;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.LConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.object.ConcFieldConversionFactory;
import com.lexicalscope.svm.j.instruction.concrete.object.GetFieldOp;
import com.lexicalscope.svm.j.instruction.concrete.object.NewObjectOp;
import com.lexicalscope.svm.j.instruction.concrete.object.PutFieldOp;
import com.lexicalscope.svm.j.instruction.concrete.ops.Binary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.BinaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.Nullary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.NullaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.UnaryOperator;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.Vop;

public class ConcInstructionFactory implements InstructionFactory {
   @Override public BinaryOperator iaddOperation() {
      return new IAddOperator();
   }

   @Override public BinaryOperator iremOperation() {
      return new IRemOperator();
   }

   @Override
   public BinaryOperator imulOperation() {
      return new IMulOperator();
   }

   @Override
   public BinaryOperator fmulOperation() {
      return new FMulOp();
   }

   @Override
   public BinaryOperator fdivOperation() {
      return new FDivOp();
   }

   @Override
   public BinaryOperator fsubOperation() {
      return new FSubOp();
   }

   @Override
   public BinaryOperator faddOperation() {
      return new FAddOp();
   }

   @Override
   public BinaryOperator isubOperation() {
      return new ISubOperator();
   }

   @Override
   public BinaryOperator idivOperation() {
      return new IDivOperator();
   }

   @Override
   public UnaryOperator inegOperation() {
      return new INegOperator();
   }

   @Override public BinaryOperator iandOperation() {
      return new IAndOperator();
   }

   @Override public Binary2Operator landOperation() {
      return new LAndOp();
   }

   @Override
   public Vop ifge(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifge());
   }

   @Override
   public Vop ifgt(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifgt());
   }

   @Override
   public Vop ifle(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifle());
   }

   @Override
   public Vop iflt(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Iflt());
   }

   @Override
   public Vop ifne(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifne());
   }

   @Override
   public Vop ifeq(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifeq());
   }

   @Override public Vop ificmpeq(final JumpInsnNode jumpInsnNode) {
      return icmp(new Ificmpeq());
   }

   @Override public Vop ificmpne(final JumpInsnNode jumpInsnNode) {
      return icmp(new Ificmpne());
   }

   @Override public Vop ificmple(final JumpInsnNode jumpInsnNode) {
      return icmp(new Ificmple());
   }

   @Override public Vop ificmpge(final JumpInsnNode jumpInsnNode) {
      return icmp(new Ificmpge());
   }

   @Override public Vop ificmplt(final JumpInsnNode jumpInsnNode) {
      return icmp(new Ificmplt());
   }

   @Override public Vop ificmpgt(final JumpInsnNode jumpInsnNode) {
      return icmp(new Ificmpgt());
   }

   @Override public Vop ifnull(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifnull());
   }

   @Override public Vop ifnonnull(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifnonnull());
   }

   @Override public Vop ifacmpne(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifacmpne());
   }

   @Override public Vop ifacmpeq(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifacmpeq());
   }

   private Vop icmp(final ICmpOp op) {
      return branchInstruction(new ICmpBranchPredicate(op));
   }

   private Vop branchInstruction(final BranchPredicate branchPredicate) {
      return new BranchInstruction(branchPredicate);
   }

   @Override
   public NullaryOperator iconst(final int val) {
      return new IConstOperator(val);
   }

   @Override
   public Nullary2Operator lconst(final long val) {
      return new LConstOperator(val);
   }

   @Override
   public NullaryOperator fconst(final float val) {
      return new FConstOperator(val);
   }

   @Override
   public Nullary2Operator dconst(final double val) {
      return new DConstOperator(val);
   }

   @Override public Object initInt() {
      return 0;
   }

   @Override public Vop putField(final FieldInsnNode fieldInsnNode) {
      return new PutFieldOp(new ConcFieldConversionFactory(), fieldInsnNode);
   }

   @Override public Vop getField(final FieldInsnNode fieldInsnNode) {
      return new GetFieldOp(new ConcFieldConversionFactory(), fieldInsnNode);
   }

   @Override public NewArrayOp newArray(final Object initialFieldValue) {
      return new NewArrayOp(initialFieldValue, new NewConcArray());
   }

   @Override public Vop aNewArray() {
      return new NewArrayOp(new NewConcArray());
   }

   @Override public Vop reflectionNewArray() {
      // TODO[tim]: take into account array type
      return aNewArray();
   }

   @Override public Vop aaStore() {
      return ArrayStoreOp.aaStore();
   }

   @Override public Vop iaStore() {
      return ArrayStoreOp.iaStore();
   }

   @Override public Vop caStore() {
      return ArrayStoreOp.caStore();
   }

   @Override public Vop iaLoad() {
      return ArrayLoadOp.iaLoad();
   }

   @Override public Vop aaLoad() {
      return ArrayLoadOp.aaLoad();
   }

   @Override public Vop caLoad() {
      return ArrayLoadOp.caLoad();
   }

   @Override public Vop loadArg(final Object object, final InstructionSource instructions) {
      return new LinearOp(new LoadConstantArg(object));
   }

   @Override public Op<?> newObject(final KlassInternalName klassDesc) {
      return new NewObjectOp(klassDesc);
   }
}
