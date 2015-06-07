package com.lexicalscope.svm.j.instruction.factory;

import static com.lexicalscope.svm.vm.j.InstructionCode.synthetic;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.lexicalscope.svm.j.instruction.UnsupportedInstruction;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class InstructionSwitch {
   private final InstructionSource s;

   public InstructionSwitch(final InstructionSource s) {
      this.s = s;
   }

   public InstructionSource instructionFor(
         final AbstractInsnNode abstractInsnNode,
         final SMethodDescriptor methodName,
         final InstructionSource.InstructionSink sink) {
      switch (abstractInsnNode.getType()) {
         case AbstractInsnNode.VAR_INSN:
            final VarInsnNode varInsnNode = (VarInsnNode) abstractInsnNode;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.LLOAD:
               case Opcodes.DLOAD:
                  return s.load2(varInsnNode.var, sink);
               case Opcodes.ILOAD:
                  return s.iload(varInsnNode.var, sink);
               case Opcodes.ALOAD:
                  return s.aload(varInsnNode.var, sink);
               case Opcodes.FLOAD:
                  return s.fload(varInsnNode.var, sink);
               case Opcodes.LSTORE:
                  return s.store2(varInsnNode.var, sink);
               case Opcodes.ISTORE:
               case Opcodes.ASTORE:
                  return s.store(varInsnNode.var, sink);
            }
            break;
         case AbstractInsnNode.FIELD_INSN:
            final FieldInsnNode fieldInsnNode = (FieldInsnNode) abstractInsnNode;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.PUTFIELD:
                  return s.putfield(fieldInsnNode, sink);
               case Opcodes.GETFIELD:
                  return s.getfield(fieldInsnNode, sink);
               case Opcodes.GETSTATIC:
                  return s.getstaticfield(fieldInsnNode, sink);
               case Opcodes.PUTSTATIC:
                  return s.putstaticfield(fieldInsnNode, sink);
            }
            break;
         case AbstractInsnNode.INSN:
            final InsnNode insnNode = (InsnNode) abstractInsnNode;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.ACONST_NULL:
                  return s.aconst_null(sink);
               case Opcodes.RETURN:
                  return s.returnvoid(methodName, sink);
               case Opcodes.IRETURN:
                  return s.return1(methodName, sink);
               case Opcodes.FRETURN:
                  return s.return1(methodName, sink);
               case Opcodes.LRETURN:
                  return s.return2(methodName, sink);
               case Opcodes.ARETURN:
                  return s.return1(methodName, sink);
               case Opcodes.ATHROW:
                  return s.athrow(sink);
               case Opcodes.IAND:
                  return s.iand(sink);
               case Opcodes.LAND:
                  return s.land(sink);
               case Opcodes.IADD:
                  return s.iadd(sink);
               case Opcodes.IREM:
                  return s.irem(sink);
               case Opcodes.IMUL:
                  return s.imul(sink);
               case Opcodes.IDIV:
                  return s.idiv(sink);
               case Opcodes.FMUL:
                  return s.fmul(sink);
               case Opcodes.FDIV:
                  return s.fdiv(sink);
               case Opcodes.ISUB:
                  return s.isub(sink);
               case Opcodes.INEG:
                  return s.ineg(sink);
               case Opcodes.DUP:
                  return s.dup(sink);
               case Opcodes.DUP_X1:
                  return s.dup_x1(sink);
               case Opcodes.ICONST_M1:
                  return s.iconst_m1(sink);
               case Opcodes.ICONST_0:
                  return s.iconst_0(sink);
               case Opcodes.ICONST_1:
                  return s.iconst_1(sink);
               case Opcodes.ICONST_2:
                  return s.iconst_2(sink);
               case Opcodes.ICONST_3:
                  return s.iconst_3(sink);
               case Opcodes.ICONST_4:
                  return s.iconst_4(sink);
               case Opcodes.ICONST_5:
                  return s.iconst_5(sink);
               case Opcodes.LCONST_0:
                  return s.lconst_0(sink);
               case Opcodes.LCONST_1:
                  return s.lconst_1(sink);
               case Opcodes.FCONST_0:
                  return s.fconst_0(sink);
               case Opcodes.CASTORE:
                  return s.castore(sink);
               case Opcodes.IASTORE:
                  return s.iastore(sink);
               case Opcodes.AASTORE:
                  return s.aastore(sink);
               case Opcodes.CALOAD:
                  return s.caload(sink);
               case Opcodes.IALOAD:
                  return s.iaload(sink);
               case Opcodes.AALOAD:
                  return s.aaload(sink);
               case Opcodes.ARRAYLENGTH:
                  return s.arraylength(sink);
               case Opcodes.ISHL:
                  return s.ishl(sink);
               case Opcodes.ISHR:
                   return s.ishr(sink);
               case Opcodes.IUSHR:
                  return s.iushr(sink);
               case Opcodes.IOR:
                  return s.ior(sink);
               case Opcodes.IXOR:
                  return s.ixor(sink);
               case Opcodes.LUSHR:
                  return s.lushr(sink);
               case Opcodes.I2L:
                  return s.i2l(sink);
               case Opcodes.L2I:
                  return s.l2i(sink);
               case Opcodes.I2F:
                  return s.i2f(sink);
               case Opcodes.F2I:
                  return s.f2i(sink);
               case Opcodes.I2C:
                  return s.i2c(sink);
               case Opcodes.FCMPG:
                  return s.fcmpg(sink);
               case Opcodes.FCMPL:
                  return s.fcmpl(sink);
               case Opcodes.LCMP:
                  return s.lcmp(sink);
               case Opcodes.POP:
                  return s.pop(sink);
            }
            break;
         case AbstractInsnNode.LDC_INSN:
            final LdcInsnNode ldcInsnNode = (LdcInsnNode) abstractInsnNode;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.LDC:
                  if(ldcInsnNode.cst instanceof Integer) {
                     return s.ldcint((int) ldcInsnNode.cst, sink);
                  } else if(ldcInsnNode.cst instanceof Long) {
                     return s.ldclong((long) ldcInsnNode.cst, sink);
                  } else if(ldcInsnNode.cst instanceof Float) {
                     return s.ldcfloat((float) ldcInsnNode.cst, sink);
                  } else if(ldcInsnNode.cst instanceof Double) {
                     return s.ldcdouble((double) ldcInsnNode.cst, sink);
                  } else if(ldcInsnNode.cst instanceof String) {
                     return s.stringpoolload((String) ldcInsnNode.cst, sink);
                  } else if(ldcInsnNode.cst instanceof Type) {
                     final Type toLoad = (Type) ldcInsnNode.cst;
                     if(toLoad.getSort() == Type.OBJECT || toLoad.getSort() == Type.ARRAY) {
                        return s.objectpoolload(toLoad,  sink);
                     }
                  }
                  // System.out.println("!!!!!!!! " + ldcInsnNode.cst + " " + ldcInsnNode.cst.getClass());
            }
            break;
         case AbstractInsnNode.INT_INSN:
            final IntInsnNode intInsnNode = (IntInsnNode) abstractInsnNode;
            final int val = intInsnNode.operand;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.SIPUSH:
                  return s.sipush(val, sink);
               case Opcodes.BIPUSH:
                  return s.bipush(val, sink);
               case Opcodes.NEWARRAY:
                  return s.newarray(val, sink);
            }
            break;
         case AbstractInsnNode.IINC_INSN:
            final IincInsnNode iincInsnNode = (IincInsnNode) abstractInsnNode;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.IINC:
                  return s.iinc(iincInsnNode, sink);
            }
            break;
         case AbstractInsnNode.TYPE_INSN:
            final TypeInsnNode typeInsnNode = (TypeInsnNode) abstractInsnNode;
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.NEW:
                  return s.newobject(internalName(typeInsnNode.desc), sink);
               case Opcodes.ANEWARRAY:
                  return s.anewarray(sink);
               case Opcodes.INSTANCEOF:
                  return s.instance0f(typeInsnNode, sink);
               case Opcodes.CHECKCAST:
                  return s.checkcast(typeInsnNode, sink);
            }
            break;
         case AbstractInsnNode.JUMP_INSN:
            final JumpInsnNode jumpInsnNode = (JumpInsnNode) abstractInsnNode;
            switch (jumpInsnNode.getOpcode()) {
               case Opcodes.IFGE:
                  return s.ifge(jumpInsnNode, sink);
               case Opcodes.IFGT:
                  return s.ifgt(jumpInsnNode, sink);
               case Opcodes.IFLE:
                  return s.ifle(jumpInsnNode, sink);
               case Opcodes.IFLT:
                  return s.iflt(jumpInsnNode, sink);
               case Opcodes.IFEQ:
                  return s.ifeq(jumpInsnNode, sink);
               case Opcodes.IFNE:
                  return s.ifne(jumpInsnNode, sink);
               case Opcodes.IFNULL:
                  return s.ifnull(jumpInsnNode, sink);
               case Opcodes.IFNONNULL:
                  return s.ifnonnull(jumpInsnNode, sink);
               case Opcodes.IF_ICMPEQ:
                  return s.ificmpeq(jumpInsnNode, sink);
               case Opcodes.IF_ICMPNE:
                  return s.ificmpne(jumpInsnNode, sink);
               case Opcodes.IF_ICMPLE:
                  return s.ificmple(jumpInsnNode, sink);
               case Opcodes.IF_ICMPLT:
                  return s.ificmplt(jumpInsnNode, sink);
               case Opcodes.IF_ICMPGT:
                  return s.ificmpgt(jumpInsnNode, sink);
               case Opcodes.IF_ICMPGE:
                  return s.ificmpge(jumpInsnNode, sink);
               case Opcodes.IF_ACMPNE:
                  return s.ifacmpne(jumpInsnNode, sink);
               case Opcodes.IF_ACMPEQ:
                  return s.ifacmpeq(jumpInsnNode, sink);
               case Opcodes.GOTO:
                  return s.got0(sink);
            }
            break;
         case AbstractInsnNode.METHOD_INSN:
            final MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
            final SMethodDescriptor name = new AsmSMethodName(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
            switch (abstractInsnNode.getOpcode()) {
               case Opcodes.INVOKESTATIC:
                  return s.invokestatic(name, sink);
               case Opcodes.INVOKESPECIAL:
                  return s.invokespecial(name, sink);
               case Opcodes.INVOKEINTERFACE:
                  return s.invokeinterface(name, sink);
               case Opcodes.INVOKEVIRTUAL:
                  return s.invokevirtual(name, sink);
            }
            break;
      }

      sink.nextOp(new UnsupportedInstruction(abstractInsnNode), synthetic);
      return s;
   }
}
