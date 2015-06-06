package com.lexicalscope.svm.j.instruction.symbolic;

import com.lexicalscope.svm.j.instruction.symbolic.ops.SIDivOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIRemOperator;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import com.lexicalscope.svm.j.instruction.LinearOp;
import com.lexicalscope.svm.j.instruction.concrete.LoadConstantArg;
import com.lexicalscope.svm.j.instruction.concrete.array.NewArrayOp;
import com.lexicalscope.svm.j.instruction.concrete.branch.BranchInstruction;
import com.lexicalscope.svm.j.instruction.concrete.branch.BranchPredicate;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifacmpne;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifnonnull;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Ifnull;
import com.lexicalscope.svm.j.instruction.concrete.d0uble.DConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.LAndOp;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.LConstOperator;
import com.lexicalscope.svm.j.instruction.concrete.object.GetFieldOp;
import com.lexicalscope.svm.j.instruction.concrete.object.NewObjectOp;
import com.lexicalscope.svm.j.instruction.concrete.object.PutFieldOp;
import com.lexicalscope.svm.j.instruction.concrete.ops.Binary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.BinaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.Nullary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.NullaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.UnaryOperator;
import com.lexicalscope.svm.j.instruction.factory.InstructionFactory;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SArrayLoadOp;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SArrayStoreOp;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIAddOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIAndOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIBinaryOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIBinaryOperatorAdapter;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIMulOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SINegOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SISubOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIUnaryOperator;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SIUnaryOperatorAdapter;
import com.lexicalscope.svm.j.instruction.symbolic.ops.SymbFieldConversionFactory;
import com.lexicalscope.svm.j.instruction.symbolic.ops.array.NewSymbArray;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayTerminalSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.OSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.OTerminalSymbol;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Op;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.z3.FeasibilityChecker;

/**
 * @author tim
 */
public class SymbInstructionFactory implements InstructionFactory {
   private final FeasibilityChecker feasibilityChecker;
   private int symbol = -1;

   public SymbInstructionFactory(final FeasibilityChecker feasbilityChecker) {
      feasibilityChecker = feasbilityChecker;
   }

   public SymbInstructionFactory() {
      this(new FeasibilityChecker());
   }

   @Override
   public BinaryOperator iaddOperation() {
      return siBinary(new SIAddOperator());
   }

   @Override
   public BinaryOperator iremOperation() {
      return siBinary(new SIRemOperator());
   }

   @Override public BinaryOperator iandOperation() {
      return siBinary(new SIAndOperator());
   }

   @Override
   public BinaryOperator imulOperation() {
      return siBinary(new SIMulOperator());
   }

   @Override
   public BinaryOperator isubOperation() {
      return siBinary(new SISubOperator());
   }

   @Override
   public BinaryOperator idivOperation() {
      return siBinary(new SIDivOperator());
   }

   private SIBinaryOperatorAdapter siBinary(final SIBinaryOperator op) {
      return new SIBinaryOperatorAdapter(op);
   }

   @Override public UnaryOperator inegOperation() {
      return siUnary(new SINegOperator());
   }

   private SIUnaryOperatorAdapter siUnary(final SIUnaryOperator op) {
      return new SIUnaryOperatorAdapter(op);
   }

   public ITerminalSymbol isymbol() {
      return new ITerminalSymbol(++symbol);
   }

   public IArrayTerminalSymbol iasymbol() {
      return new IArrayTerminalSymbol(++symbol);
   }

   public OSymbol osymbol(final String klassName) {
      return new OTerminalSymbol(++symbol, klassName);
   }

   @Override
   public Vop ifge(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.geInstruction(feasibilityChecker);
   }

   @Override public Vop ifgt(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.gtInstruction(feasibilityChecker);
   }

   @Override
   public Vop ifle(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.leInstruction(feasibilityChecker);
   }

   @Override
   public Vop iflt(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.ltInstruction(feasibilityChecker);
   }

   @Override public Vop ifne(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.neInstruction(feasibilityChecker);
   }

   @Override public Vop ifeq(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.eqInstruction(feasibilityChecker);
   }

   @Override public Vop ificmpeq(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.icmpeqInstruction(feasibilityChecker);
   }

   @Override public Vop ificmpne(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.icmpneInstruction(feasibilityChecker);
   }

   @Override public Vop ificmple(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.icmpleInstruction(feasibilityChecker);
   }

   @Override public Vop ificmpge(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.icmpgeInstruction(feasibilityChecker);
   }

   @Override public Vop ificmplt(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.icmpltInstruction(feasibilityChecker);
   }

   @Override public Vop ificmpgt(final JumpInsnNode jumpInsnNode) {
      return SBranchInstruction.icmpgtInstruction(feasibilityChecker);
   }

   @Override public Vop ifacmpeq(final JumpInsnNode jumpInsnNode) {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override public Vop ifacmpne(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifacmpne());
   }

   @Override
   public NullaryOperator iconst(final int val) {
      return new IConstOperator(val);
   }

   @Override public Nullary2Operator lconst(final long val) {
      return new LConstOperator(val);
   }

   @Override public NullaryOperator fconst(final float val) {
      return new FConstOperator(val);
   }

   @Override public Vop ifnonnull(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifnonnull());
   }

   @Override public Vop ifnull(final JumpInsnNode jumpInsnNode) {
      return branchInstruction(new Ifnull());
   }

   private Vop branchInstruction(final BranchPredicate branchPredicate) {
      return new BranchInstruction(branchPredicate);
   }

   @Override public Object initInt() {
      return 0;
   }

   @Override public BinaryOperator fmulOperation() {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override public BinaryOperator fdivOperation() {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override public BinaryOperator faddOperation() {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override public BinaryOperator fsubOperation() {
      throw new UnsupportedOperationException("not implemented yet");
   }

   @Override public Nullary2Operator dconst(final double val) {
      return new DConstOperator(val);
   }

   @Override public Binary2Operator landOperation() {
      return new LAndOp();
   }

   @Override public Vop putField(final FieldInsnNode fieldInsnNode) {
      return new PutFieldOp(new SymbFieldConversionFactory(), fieldInsnNode);
   }

   @Override public Vop getField(final FieldInsnNode fieldInsnNode) {
      return new GetFieldOp(new SymbFieldConversionFactory(), fieldInsnNode);
   }

   @Override public NewArrayOp newArray(final Object initialFieldValue) {
      return new NewArrayOp(initialFieldValue, new NewSymbArray(feasibilityChecker));
   }

   @Override public Vop aNewArray() {
      return new NewArrayOp(new NewSymbArray(feasibilityChecker));
   }

   @Override public Vop reflectionNewArray() {
      // TODO[tim]: take into account array type
      return aNewArray();
   }

   @Override public Vop aaStore() {
      return SArrayStoreOp.aaStore(feasibilityChecker);
   }

   @Override public Vop iaStore() {
      return SArrayStoreOp.iaStore(feasibilityChecker);
   }

   @Override public Vop caStore() {
      return SArrayStoreOp.caStore(feasibilityChecker);
   }

   @Override public Vop aaLoad() {
      return SArrayLoadOp.aaLoad(feasibilityChecker);
   }

   @Override public Vop iaLoad() {
      return SArrayLoadOp.iaLoad(feasibilityChecker);
   }

   @Override public Vop caLoad() {
      return SArrayLoadOp.caLoad(feasibilityChecker);
   }

   @Override public Vop loadArg(final Object object, final InstructionSource instructions) {
//      if(object instanceof OTerminalSymbol) {
//         final OTerminalSymbol terminalSymbol = (OTerminalSymbol)object;
//         return new LoadingOp(terminalSymbol.klass(), new LoadSymbolicObjectArg(terminalSymbol), instructions);
//      } else {
      if (object instanceof IArrayAndLengthSymbols) {
         return new LinearOp(
               new LoadSymbolicArrayArg((IArrayAndLengthSymbols) object));
      } else {
         return new LinearOp(new LoadConstantArg(object));
      }
   }

   @Override public Op<?> newObject(final KlassInternalName klassDesc) {
      return new NewObjectOp(klassDesc);
   }
}
