package com.lexicalscope.svm.j.instruction.factory;

import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import com.lexicalscope.svm.j.statementBuilder.StatementBuilder;
import com.lexicalscope.symb.vm.j.Instruction;
import com.lexicalscope.symb.vm.j.Vop;
import com.lexicalscope.symb.vm.j.j.klass.SMethodDescriptor;

public interface InstructionSource {
   public interface InstructionSink {
      void nextInstruction(Instruction node);
      void linearOp(Vop op);
      void loadingOp(List<String> classes, Vop op, InstructionSource factory);
      void nextOp(Vop op);
      void noOp();
   }

   /**
    * useful for instrumentation of method entries
    */
   InstructionSource methodentry(InstructionSink sink);

   InstructionSource load(int var, InstructionSink sink);
   InstructionSource load2(int var, InstructionSink sink);
   InstructionSource aload(int index, InstructionSink sink);
   InstructionSource fload(int index, InstructionSink sink);
   InstructionSource dload(int index, InstructionSink sink);
   InstructionSource store(int var, InstructionSink sink);
   InstructionSource store2(int var, InstructionSink sink);

   InstructionSource putField(FieldInsnNode fieldInsnNode, InstructionSink sink);
   InstructionSource getField(FieldInsnNode fieldInsnNode, InstructionSink sink);
   InstructionSource getStaticField(FieldInsnNode fieldInsnNode, InstructionSink sink);
   InstructionSource putStaticField(FieldInsnNode fieldInsnNode, InstructionSink sink);

   InstructionSource aconst_null(InstructionSink sink);

   InstructionSource returnVoid(InstructionSink sink);
   InstructionSource return1(InstructionSink sink);
   InstructionSource return2(InstructionSink sink);

   InstructionSource iand(InstructionSink sink);
   InstructionSource iadd(InstructionSink sink);
   InstructionSource imul(InstructionSink sink);
   InstructionSource isub(InstructionSink sink);
   InstructionSource ineg(InstructionSink sink);
   InstructionSource ishl(InstructionSink sink);
   InstructionSource ishr(InstructionSink sink);
   InstructionSource iushr(InstructionSink sink);
   InstructionSource ior(InstructionSink sink);
   InstructionSource ixor(InstructionSink sink);
   InstructionSource lushr(InstructionSink sink);
   InstructionSource iinc(IincInsnNode iincInsnNode, InstructionSink sink);
   InstructionSource i2l(InstructionSink sink);
   InstructionSource i2f(InstructionSink sink);
   InstructionSource iconst(int val, InstructionSink sink);
   InstructionSource iconst_m1(InstructionSink sink);
   InstructionSource iconst_0(InstructionSink sink);
   InstructionSource iconst_1(InstructionSink sink);
   InstructionSource iconst_2(InstructionSink sink);
   InstructionSource iconst_3(InstructionSink sink);
   InstructionSource iconst_4(InstructionSink sink);
   InstructionSource iconst_5(InstructionSink sink);
   InstructionSource ifge(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ifgt(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ifle(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource iflt(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ifeq(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ifne(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ificmpeq(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ificmpne(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ificmple(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ificmplt(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ificmpgt(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ificmpge(JumpInsnNode jumpInsnNode, InstructionSink sink);

   InstructionSource fmul(InstructionSink sink);
   InstructionSource fdiv(InstructionSink sink);
   InstructionSource f2i(InstructionSink sink);
   InstructionSource fcmpg(InstructionSink sink);
   InstructionSource fcmpl(InstructionSink sink);
   InstructionSource fconst_0(InstructionSink sink);

   InstructionSource land(InstructionSink sink);
   InstructionSource lconst(long val, InstructionSink sink);
   InstructionSource lconst_0(InstructionSink sink);
   InstructionSource lconst_1(InstructionSink sink);
   InstructionSource l2i(InstructionSink sink);
   InstructionSource lcmp(InstructionSink sink);

   InstructionSource sipush(int val, InstructionSink sink);
   InstructionSource bipush(int val, InstructionSink sink);

   InstructionSource dup(InstructionSink sink);
   InstructionSource dup_x1(InstructionSink sink);
   InstructionSource pop(InstructionSink sink);

   InstructionSource newarray(int val, InstructionSink sink);
   InstructionSource anewarray(InstructionSink sink);
   InstructionSource reflectionnewarray(InstructionSink sink);
   InstructionSource caStore(InstructionSink sink);
   InstructionSource iaStore(InstructionSink sink);
   InstructionSource aaStore(InstructionSink sink);
   InstructionSource caload(InstructionSink sink);
   InstructionSource iaload(InstructionSink sink);
   InstructionSource aaload(InstructionSink sink);
   InstructionSource arrayLength(InstructionSink sink);

   InstructionSource ldcInt(int cst, InstructionSink sink);
   InstructionSource ldcLong(long cst, InstructionSink sink);
   InstructionSource ldcFloat(float cst, InstructionSink sink);
   InstructionSource ldcDouble(double cst, InstructionSink sink);
   InstructionSource stringPoolLoad(String cst, InstructionSink sink);
   InstructionSource objectPoolLoad(Type toLoad, InstructionSink sink);

   InstructionSource newObject(String desc, InstructionSink sink);
   InstructionSource instance0f(TypeInsnNode typeInsnNode, InstructionSink sink);
   InstructionSource checkcast(TypeInsnNode typeInsnNode, InstructionSink sink);
   InstructionSource ifnull(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ifnonnull(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource ifacmpne(JumpInsnNode jumpInsnNode, InstructionSink sink);
   InstructionSource invokestatic(SMethodDescriptor name, InstructionSink sink);
   InstructionSource invokespecial(SMethodDescriptor name, InstructionSink sink);
   InstructionSource invokeinterface(SMethodDescriptor name, InstructionSink sink);
   InstructionSource invokevirtual(SMethodDescriptor name, InstructionSink sink);

   InstructionSource got0(InstructionSink sink);

   /**
    * The JVM is responsible for creating class objects (that is objects that represent the
    * class of other objects).
    */
   InstructionSource invokeConstructorOfClassObjects(String klassName, InstructionSink sink);

   /**
    * Instruction for loading the initial arguments that we will pass to the entry point
    */
   InstructionSource loadArg(Object object, InstructionSink sink);

   /**
    * The initial value of fields of the given type
    */
   Object initialFieldValue(String desc);

   // TODO[tim]: these methods are too hard to wrap
   StatementBuilder statements();
   StatementBuilder before(Instruction nextInstruction);
}
