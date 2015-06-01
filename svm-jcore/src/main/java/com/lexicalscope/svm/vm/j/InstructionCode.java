package com.lexicalscope.svm.vm.j;

import java.util.EnumSet;
import java.util.Set;



public enum InstructionCode {
   methodentry,
   synthetic,
   methodexit,

   load,
   load2,
   aload,
   iload,
   fload,
   dload,
   store,
   store2,

   putfield,
   getfield,
   getstaticfield,
   putstaticfield,

   aconst_null,

   returnvoid,
   return1,
   return2,

   iand,
   iadd,
   irem,
   imul,
   isub,
   ineg,
   ishl,
   ishr,
   iushr,
   ior,
   ixor,
   lushr,
   iinc,
   i2c,
   i2l,
   i2f,
   iconst,
   iconst_m1,
   iconst_0,
   iconst_1,
   iconst_2,
   iconst_3,
   iconst_4,
   iconst_5,
   ifge,
   ifgt,
   ifle,
   iflt,
   ifeq,
   ifne,
   ificmpeq,
   ificmpne,
   ificmple,
   ificmplt,
   ificmpgt,
   ificmpge,

   fmul,
   fdiv,
   f2i,
   fcmpg,
   fcmpl,
   fconst_0,

   land,
   lconst,
   lconst_0,
   lconst_1,
   l2i,
   lcmp,

   sipush,
   bipush,

   dup,
   dup_x1,
   pop,

   newarray,
   anewarray,
   reflectionnewarray,
   castore,
   iastore,
   aastore,
   caload,
   iaload,
   aaload,
   arraylength,

   ldcint,
   ldclong,
   ldcfloat,
   ldcdouble,
   stringpoolload,
   objectpoolload,

   newobject,
   instance0f,
   checkcast,
   ifnull,
   ifnonnull,
   ifacmpeq,
   ifacmpne,
   invokestatic,
   invokespecial,
   invokeinterface,
   invokevirtual,

   got0,

   invokeconstructorofclassobjects,

   loadarg;

   public static final Set<InstructionCode> calls = EnumSet.of(
         invokeinterface, invokespecial, invokevirtual, invokestatic);
   public static final Set<InstructionCode> dynamicCalls = EnumSet.of(
         invokeinterface, invokespecial, invokevirtual);
   public static final Set<InstructionCode> returns = EnumSet.of(return1, return2, returnvoid);

   public boolean isReturn() { return returns.contains(this); }
   public boolean isMethodExit() { return this.equals(methodexit); }
   public boolean isMethodCall() { return calls.contains(this); }
   public boolean isDynamicMethodCall() { return dynamicCalls.contains(this); }
}
