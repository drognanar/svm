package com.lexicalscope.svm.j.instruction.factory;

import static com.lexicalscope.svm.vm.j.InstructionCode.*;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.j.instruction.concrete.integer.I2COp;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.lexicalscope.svm.j.instruction.MethodEntryVop;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayLengthOp;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayLoadOp;
import com.lexicalscope.svm.j.instruction.concrete.array.ArrayStoreOp;
import com.lexicalscope.svm.j.instruction.concrete.branch.BranchInstruction;
import com.lexicalscope.svm.j.instruction.concrete.branchPredicates.Got0;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.F2IOp;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FCmpGOperator;
import com.lexicalscope.svm.j.instruction.concrete.fl0at.FCmpLOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.I2FOp;
import com.lexicalscope.svm.j.instruction.concrete.integer.I2LOp;
import com.lexicalscope.svm.j.instruction.concrete.integer.IincOp;
import com.lexicalscope.svm.j.instruction.concrete.integer.IorOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IshlOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IshrOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IushrOperator;
import com.lexicalscope.svm.j.instruction.concrete.integer.IxorOperator;
import com.lexicalscope.svm.j.instruction.concrete.klass.CheckCastOp;
import com.lexicalscope.svm.j.instruction.concrete.klass.GetStaticOp;
import com.lexicalscope.svm.j.instruction.concrete.klass.InstanceOfOp;
import com.lexicalscope.svm.j.instruction.concrete.klass.LoadingOp;
import com.lexicalscope.svm.j.instruction.concrete.klass.PutStaticOp;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.L2IOp;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.LCmpOp;
import com.lexicalscope.svm.j.instruction.concrete.l0ng.LushrOp;
import com.lexicalscope.svm.j.instruction.concrete.method.MethodCallInstruction;
import com.lexicalscope.svm.j.instruction.concrete.object.AConstNullOp;
import com.lexicalscope.svm.j.instruction.concrete.ops.Binary2Op;
import com.lexicalscope.svm.j.instruction.concrete.ops.BinaryOp;
import com.lexicalscope.svm.j.instruction.concrete.ops.BinaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.Nullary2Op;
import com.lexicalscope.svm.j.instruction.concrete.ops.Nullary2Operator;
import com.lexicalscope.svm.j.instruction.concrete.ops.NullaryOp;
import com.lexicalscope.svm.j.instruction.concrete.ops.NullaryOperator;
import com.lexicalscope.svm.j.instruction.concrete.ops.UnaryOp;
import com.lexicalscope.svm.j.instruction.concrete.pool.ObjectPoolLoad;
import com.lexicalscope.svm.j.instruction.concrete.pool.StringPoolLoadOperator;
import com.lexicalscope.svm.j.instruction.concrete.stack.DupOp;
import com.lexicalscope.svm.j.instruction.concrete.stack.Dup_X1Op;
import com.lexicalscope.svm.j.instruction.concrete.stack.Load;
import com.lexicalscope.svm.j.instruction.concrete.stack.Load2;
import com.lexicalscope.svm.j.instruction.concrete.stack.PopOp;
import com.lexicalscope.svm.j.instruction.concrete.stack.ReturnInstruction;
import com.lexicalscope.svm.j.instruction.concrete.stack.Store;
import com.lexicalscope.svm.j.instruction.concrete.stack.Store2;
import com.lexicalscope.svm.vm.j.InstructionCode;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class BaseInstructionSource implements InstructionSource {
   private final InstructionFactory instructionFactory;

   public BaseInstructionSource(final InstructionFactory instructionFactory) {
      this.instructionFactory = instructionFactory;
   }

   @Override public InstructionSource methodentry(final SMethodDescriptor methodName, final InstructionSink sink) {
      linearInstruction(new MethodEntryVop(methodName), methodentry, sink);
      return this;
   }

   @Override
   public InstructionSource invokevirtual(final SMethodDescriptor name, final InstructionSource.InstructionSink sink) {
      MethodCallInstruction.invokevirtual(name, sink);
      return this;
   }

   @Override
   public InstructionSource invokeinterface(final SMethodDescriptor name, final InstructionSource.InstructionSink sink) {
      MethodCallInstruction.invokeinterface(name, sink);
      return this;
   }

   @Override
   public InstructionSource invokespecial(final SMethodDescriptor name, final InstructionSource.InstructionSink sink) {
      MethodCallInstruction.invokespecial(name, sink);
      return this;
   }

   @Override
   public InstructionSource invokestatic(final SMethodDescriptor name, final InstructionSource.InstructionSink sink) {
      MethodCallInstruction.invokestatic(name, sink, this);
      return this;
   }

   @Override public InstructionSource invokeconstructorofclassobjects(final KlassInternalName klassName, final InstructionSource.InstructionSink sink) {
      MethodCallInstruction.invokeconstructorofclassobjects(klassName, sink);
      return this;
   }

   @Override
   public InstructionSource got0(final InstructionSource.InstructionSink sink) {
      return sink(new BranchInstruction(new Got0()), got0, sink);
   }

   @Override
   public InstructionSource ifacmpne(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifacmpne(jumpInsnNode), ifacmpne, sink);
   }

   @Override
   public InstructionSource ifacmpeq(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifacmpeq(jumpInsnNode), ifacmpeq, sink);
   }

   @Override
   public InstructionSource ificmpge(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ificmpge(jumpInsnNode), ificmpge, sink);
   }

   @Override
   public InstructionSource ificmpgt(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ificmpgt(jumpInsnNode), ificmpgt, sink);
   }

   @Override
   public InstructionSource ificmplt(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ificmplt(jumpInsnNode), ificmplt, sink);
   }

   @Override
   public InstructionSource ificmple(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ificmple(jumpInsnNode), ificmple, sink);
   }

   @Override
   public InstructionSource ificmpne(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ificmpne(jumpInsnNode), ificmpne, sink);
   }

   @Override
   public InstructionSource ificmpeq(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ificmpeq(jumpInsnNode), ificmpeq, sink);
   }

   @Override
   public InstructionSource ifnonnull(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifnonnull(jumpInsnNode), ifnonnull, sink);
   }

   @Override
   public InstructionSource ifnull(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifnull(jumpInsnNode), ifnull, sink);
   }

   @Override
   public InstructionSource ifne(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifne(jumpInsnNode), ifne, sink);
   }

   @Override
   public InstructionSource ifeq(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifeq(jumpInsnNode), ifeq, sink);
   }

   @Override
   public InstructionSource iflt(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.iflt(jumpInsnNode), iflt, sink);
   }

   @Override
   public InstructionSource ifle(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifle(jumpInsnNode), ifle, sink);
   }

   @Override
   public InstructionSource ifgt(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifgt(jumpInsnNode), ifgt, sink);
   }

   @Override
   public InstructionSource ifge(final JumpInsnNode jumpInsnNode, final InstructionSource.InstructionSink sink) {
      return sink(instructionFactory.ifge(jumpInsnNode), ifge, sink);
   }

   private InstructionSource sink(final Vop op, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      sink.nextOp(op, code);
      return this;
   }

   @Override
   public InstructionSource checkcast(final TypeInsnNode typeInsnNode, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new CheckCastOp(internalName(typeInsnNode.desc)), checkcast, sink);
   }

   @Override
   public InstructionSource instance0f(final TypeInsnNode typeInsnNode, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new InstanceOfOp(internalName(typeInsnNode.desc)), instance0f, sink);
   }

   @Override
   public InstructionSource anewarray(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.aNewArray(), anewarray, sink);
   }

   @Override
   public InstructionSource reflectionnewarray(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.reflectionNewArray(), reflectionnewarray, sink);
   }

   @Override
   public InstructionSource iinc(final IincInsnNode iincInsnNode, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new IincOp(iincInsnNode.var, iincInsnNode.incr), iinc, sink);
   }

   @Override
   public InstructionSource newarray(final int val, final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.newArray(initialFieldValue(val)), newarray, sink);
   }

   private Object initialFieldValue(final int atype) {
      /*
       * Array Type  atype
       * T_BOOLEAN    4
       * T_CHAR       5
       * T_FLOAT      6
       * T_DOUBLE     7
       * T_BYTE       8
       * T_SHORT      9
       * T_INT       10
       * T_LONG      11
       */
      switch (atype) {
         case 4:
            return false;
         case 5:
            return (char) '\u0000';
         case 6:
            return (float) 0f;
         case 7:
            return (double) 0d;
         case 8:
            return (byte) 0;
         case 9:
            return (short) 0;
         case 10:
            return instructionFactory.initInt();
         case 11:
            return (long) 0l;
      }
      throw new UnsupportedOperationException("" + atype);
   }

   @Override
   public InstructionSource bipush(final int val, final InstructionSource.InstructionSink sink) {
      return iconst(val, bipush, sink);
   }

   @Override
   public InstructionSource sipush(final int val, final InstructionSource.InstructionSink sink) {
      return iconst(val, sipush, sink);
   }

   @Override
   public InstructionSource ldcdouble(final double val, final InstructionSource.InstructionSink sink) {
      return linearInstruction(nullary2(instructionFactory.dconst(val)), ldcdouble, sink);
   }

   @Override
   public InstructionSource ldcfloat(final float val, final InstructionSource.InstructionSink sink) {
      return linearInstruction(fconst(val), ldcfloat, sink);
   }

   @Override
   public InstructionSource ldclong(final long val, final InstructionSource.InstructionSink sink) {
      return lconst(val, ldclong, sink);
   }

   @Override
   public InstructionSource ldcint(final int val, final InstructionSource.InstructionSink sink) {
      return iconst(val, ldcint, sink);
   }

   @Override
   public InstructionSource pop(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new PopOp(), pop, sink);
   }

   @Override
   public InstructionSource lcmp(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new LCmpOp(), lcmp, sink);
   }

   @Override
   public InstructionSource fcmpl(final InstructionSource.InstructionSink sink) {
      return binaryOp(new FCmpLOperator(), fcmpl, sink);
   }

   @Override
   public InstructionSource fcmpg(final InstructionSource.InstructionSink sink) {
      return binaryOp(new FCmpGOperator(), fcmpg, sink);
   }

   @Override
   public InstructionSource f2i(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new F2IOp(), f2i, sink);
   }

   @Override
   public InstructionSource i2f(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new I2FOp(), i2f, sink);
   }

   @Override
   public InstructionSource l2i(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new L2IOp(), l2i, sink);
   }

   @Override
   public InstructionSource i2c(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new I2COp(), i2c, sink);
   }

   @Override
   public InstructionSource i2l(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new I2LOp(), i2l, sink);
   }

   @Override
   public InstructionSource lushr(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new LushrOp(), lushr, sink);
   }

   @Override
   public InstructionSource ixor(final InstructionSource.InstructionSink sink) {
      return binaryOp(new IxorOperator(), ixor, sink);
   }

   @Override
   public InstructionSource ior(final InstructionSource.InstructionSink sink) {
      return binaryOp(new IorOperator(), ior, sink);
   }

   @Override
   public InstructionSource iushr(final InstructionSource.InstructionSink sink) {
      return binaryOp(new IushrOperator(), iushr, sink);
   }

   @Override
   public InstructionSource ishr(final InstructionSource.InstructionSink sink) {
      return binaryOp(new IshrOperator(), ishr, sink);
   }

   @Override
   public InstructionSource ishl(final InstructionSource.InstructionSink sink) {
      return binaryOp(new IshlOperator(), ishl, sink);
   }

   @Override
   public InstructionSource arraylength(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new ArrayLengthOp(), arraylength, sink);
   }

   @Override
   public InstructionSource aaload(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.aaLoad(), aaload, sink);
   }

   @Override
   public InstructionSource iaload(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.iaLoad(), iaload, sink);
   }

   @Override
   public InstructionSource caload(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.caLoad(), caload, sink);
   }

   @Override
   public InstructionSource aastore(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.aaStore(), aastore, sink);
   }

   @Override
   public InstructionSource iastore(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.iaStore(), iastore, sink);
   }

   @Override
   public InstructionSource castore(final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.caStore(), castore, sink);
   }

   @Override public InstructionSource lconst(final long val, final InstructionSource.InstructionSink sink) {
      return lconst(val, lconst, sink);
   }

   private InstructionSource lconst(final long val, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      return linearInstruction(nullary2(instructionFactory.lconst(val)), code , sink);
   }

   @Override
   public InstructionSource lconst_1(final InstructionSource.InstructionSink sink) {
      return lconst(1, lconst_1, sink);
   }

   @Override
   public InstructionSource lconst_0(final InstructionSource.InstructionSink sink) {
      return lconst(0, lconst_0, sink);
   }

   @Override
   public InstructionSource iconst_5(final InstructionSource.InstructionSink sink) {
      return iconst(5, iconst_5, sink);
   }

   @Override
   public InstructionSource iconst_4(final InstructionSource.InstructionSink sink) {
      return iconst(4, iconst_4, sink);
   }

   @Override
   public InstructionSource iconst_3(final InstructionSource.InstructionSink sink) {
      return iconst(3, iconst_3, sink);
   }

   @Override
   public InstructionSource iconst_2(final InstructionSource.InstructionSink sink) {
      return iconst(2, iconst_2, sink);
   }

   @Override
   public InstructionSource iconst_1(final InstructionSource.InstructionSink sink) {
      return iconst(1, iconst_1, sink);
   }

   @Override
   public InstructionSource iconst_m1(final InstructionSource.InstructionSink sink) {
      return iconst(-1, iconst_m1, sink);
   }

   @Override
   public InstructionSource dup_x1(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new Dup_X1Op(), dup_x1, sink);
   }

   @Override
   public InstructionSource dup(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new DupOp(), dup, sink);
   }

   @Override
   public InstructionSource ineg(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new UnaryOp(instructionFactory.inegOperation()), ineg, sink);
   }

   @Override
   public InstructionSource isub(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.isubOperation(), isub, sink);
   }

   @Override
   public InstructionSource fdiv(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.fdivOperation(), fdiv, sink);
   }

   @Override
   public InstructionSource fmul(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.fmulOperation(), fmul, sink);
   }

   @Override
   public InstructionSource imul(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.imulOperation(), imul, sink);
   }

   @Override
   public InstructionSource iadd(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.iaddOperation(), iadd, sink);
   }

   public InstructionSource irem(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.iremOperation(), irem, sink);
   }

   @Override
   public InstructionSource land(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new Binary2Op(instructionFactory.landOperation()), land, sink);
   }

   @Override
   public InstructionSource iand(final InstructionSource.InstructionSink sink) {
      return binaryOp(instructionFactory.iandOperation(), iand, sink);
   }

   @Override
   public InstructionSource putstaticfield(final FieldInsnNode fieldInsnNode, final InstructionSource.InstructionSink sink) {
      return loadingInstruction(internalName(fieldInsnNode.owner), new PutStaticOp(fieldInsnNode), putstaticfield, sink);
   }

   @Override
   public InstructionSource getstaticfield(final FieldInsnNode fieldInsnNode, final InstructionSource.InstructionSink sink) {
      return loadingInstruction(internalName(fieldInsnNode.owner), new GetStaticOp(fieldInsnNode), getstaticfield, sink);
   }

   @Override
   public InstructionSource getfield(final FieldInsnNode fieldInsnNode, final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.getField(fieldInsnNode), getfield, sink);
   }

   @Override
   public InstructionSource putfield(final FieldInsnNode fieldInsnNode, final InstructionSource.InstructionSink sink) {
      return linearInstruction(instructionFactory.putField(fieldInsnNode), putfield, sink);
   }

   @Override
   public InstructionSource store(final int var, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new Store(var), store, sink);
   }

   @Override
   public InstructionSource store2(final int var, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new Store2(var), store2, sink);
   }

   @Override public InstructionSource aconst_null(final InstructionSource.InstructionSink sink) {
      return linearInstruction(new AConstNullOp(), aconst_null, sink);
   }

   @Override public InstructionSource iconst(final int val, final InstructionSource.InstructionSink sink) {
      return iconst(val, iconst, sink);
   }

   private InstructionSource iconst(final int val, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      return linearInstruction(nullary(instructionFactory.iconst(val)), code, sink);
   }

   @Override public InstructionSource iconst_0(final InstructionSource.InstructionSink sink) {
      return iconst(0, iconst_0, sink);
   }
   @Override public InstructionSource fconst_0(final InstructionSource.InstructionSink sink) {
      return linearInstruction(fconst(0), fconst_0, sink);
   }

   @Override public InstructionSource stringpoolload(final String constVal, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new StringPoolLoadOperator(constVal), stringpoolload, sink);
   }

   @Override public InstructionSource objectpoolload(final Type constVal, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new ObjectPoolLoad(constVal), objectpoolload, sink);
   }

   @Override public InstructionSource load(final int index, final InstructionSource.InstructionSink sink) {
      return load(index, load, sink);
   }

   private InstructionSource load(final int index, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new Load(index), code, sink);
   }

   @Override public InstructionSource load2(final int index, final InstructionSource.InstructionSink sink) {
      return linearInstruction(new Load2(index), load2, sink);
   }

   @Override public InstructionSource returnvoid(final SMethodDescriptor methodName, final InstructionSource.InstructionSink sink) {
      sink.nextOp(new ReturnInstruction(methodName, 0), returnvoid);
      return this;
   }

   @Override public InstructionSource return1(final SMethodDescriptor methodName, final InstructionSource.InstructionSink sink) {
      sink.nextOp(new ReturnInstruction(methodName, 1), return1);
      return this;
   }

   @Override public InstructionSource return2(final SMethodDescriptor methodName, final InstructionSource.InstructionSink sink) {
      sink.nextOp(new ReturnInstruction(methodName, 2), return2);
      return this;
   }

   @Override public InstructionSource newobject(final KlassInternalName klassDesc, final InstructionSource.InstructionSink sink) {
      return loadingInstruction(klassDesc, new VopAdapter(instructionFactory.newObject(klassDesc)), newobject, sink);
   }

   private InstructionSource binaryOp(final BinaryOperator operation, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      sink.linearOp(new BinaryOp(operation), code);
      return this;
   }

   private InstructionSource linearInstruction(final Vop op, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      sink.linearOp(op, code);
      return this;
   }

   private InstructionSource loadingInstruction(final KlassInternalName klassDesc, final Vop op, final InstructionCode code, final InstructionSource.InstructionSink sink) {
      sink.nextOp(new LoadingOp(klassDesc, this), code);
      sink.linearOp(op, code);
      return this;
   }

   @Override public InstructionSource aload(final int index, final InstructionSource.InstructionSink sink) {
      return load(index, aload, sink);
   }

   @Override public InstructionSource fload(final int index, final InstructionSource.InstructionSink sink) {
      return load(index, fload, sink);
   }

   @Override public InstructionSource iload(final int index, final InstructionSource.InstructionSink sink) {
      return load(index, iload, sink);
   }

   @Override public InstructionSource dload(final int index, final InstructionSource.InstructionSink sink) {
      return load2(index, sink);
   }

   private NullaryOp fconst(final float constVal) {
      return nullary(instructionFactory.fconst(constVal));
   }

   private NullaryOp nullary(final NullaryOperator nullary) {
      return new NullaryOp(nullary);
   }

   private Nullary2Op nullary2(final Nullary2Operator nullary) {
      return new Nullary2Op(nullary);
   }

   @Override public InstructionSource loadarg(final Object object, final InstructionSink sink) {
      sink.nextOp(instructionFactory.loadArg(object, this), loadarg);
      return this;
   }

   @Override public Object initialFieldValue(final String desc) {
      final Type type = Type.getType(desc);
      final int sort = type.getSort();
      switch (sort) {
         case Type.OBJECT:
            return null;
         case Type.ARRAY:
            return null;
         case Type.CHAR:
            return (char) '\u0000';
         case Type.BYTE:
            return (byte) 0;
         case Type.SHORT:
            return (short) 0;
         case Type.INT:
            return instructionFactory.initInt();
         case Type.LONG:
            return 0L;
         case Type.FLOAT:
            return 0f;
         case Type.DOUBLE:
            return 0d;
         case Type.BOOLEAN:
            return false;
      }
      throw new UnsupportedOperationException("" + sort);
   }
}
