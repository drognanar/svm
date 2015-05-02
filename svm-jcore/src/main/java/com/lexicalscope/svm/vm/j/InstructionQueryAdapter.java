package com.lexicalscope.svm.vm.j;

import com.lexicalscope.svm.vm.j.klass.SFieldName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class InstructionQueryAdapter<T> implements InstructionQuery<T> {
   protected T defaultResult() {
      return null;
   }

   @Override public T nativ3() {
      return defaultResult();
   }

   @Override public T synthetic() {
      return defaultResult();
   }

   @Override public T methodentry(final SMethodDescriptor methodName) {
      return defaultResult();
   }

   @Override public T methodexit() {
      return defaultResult();
   }

   @Override public T arraycopy() {
      return defaultResult();
   }

   @Override public T arraylength() {
      return defaultResult();
   }

   @Override public T arrayload() {
      return defaultResult();
   }

   @Override public T arraystore() {
      return defaultResult();
   }

   @Override public T newarray() {
      return defaultResult();
   }

   @Override public T branch() {
      return defaultResult();
   }

   @Override public T f2i() {
      return defaultResult();
   }

   @Override public T iinc() {
      return defaultResult();
   }

   @Override public T i2f() {
      return defaultResult();
   }

   @Override public T i2l() {
      return defaultResult();
   }

   @Override public T checkcast() {
      return defaultResult();
   }

   @Override public T getstatic() {
      return defaultResult();
   }

   @Override public T instance0f() {
      return defaultResult();
   }

   @Override public T l2i() {
      return defaultResult();
   }

   @Override public T lcmp() {
      return defaultResult();
   }

   @Override public T lushr() {
      return defaultResult();
   }

   @Override public T invokevirtual(final SMethodDescriptor methodName, final MethodArguments methodArguments) {
      return defaultResult();
   }

   @Override public T invokestatic(final SMethodDescriptor methodName, final MethodArguments methodArguments) {
      return defaultResult();
   }

   @Override public T invokespecial(final SMethodDescriptor methodName, final MethodArguments methodArguments) {
      return defaultResult();
   }

   @Override public T invokeinterface(final SMethodDescriptor methodName, final MethodArguments methodArguments) {
      return defaultResult();
   }

   @Override public T aconst_null() {
      return defaultResult();
   }

   @Override public T getfield(final SFieldName name) {
      return defaultResult();
   }

   @Override public T putfield(final SFieldName name) {
      return defaultResult();
   }

   @Override public T binaryop() {
      return defaultResult();
   }

   @Override public T nularyop() {
      return defaultResult();
   }

   @Override public T unaryop() {
      return defaultResult();
   }

   @Override public T objectpoolload() {
      return defaultResult();
   }

   @Override public T stringpoolload() {
      return defaultResult();
   }

   @Override public T load(final int var) {
      return defaultResult();
   }

   @Override public T store(final int var) {
      return defaultResult();
   }

   @Override public T dup_x1() {
      return defaultResult();
   }

   @Override public T pop() {
      return defaultResult();
   }

   @Override public T r3turn(final SMethodDescriptor methodName, final int returnCount) {
      return defaultResult();
   }

   @Override public T dup() {
      return defaultResult();
   }

   @Override public T ifacmpeq() {
      return defaultResult();
   }

   @Override public T ifle() {
      return defaultResult();
   }

   @Override public T ifacmpne() {
      return defaultResult();
   }

   @Override public T ifeq() {
      return defaultResult();
   }

   @Override public T iflt() {
      return defaultResult();
   }

   @Override public T ifne() {
      return defaultResult();
   }

   @Override public T ifnonnull() {
      return defaultResult();
   }

   @Override public T ifnull() {
      return defaultResult();
   }

   @Override public T icmp() {
      return defaultResult();
   }

   @Override public T ifgt() {
      return defaultResult();
   }

   @Override public T got0() {
      return defaultResult();
   }

   @Override public T ifge() {
      return defaultResult();
   }

   @Override public T putstatic() {
      return defaultResult();
   }

   @Override public T loadarg(final Object value) {
      return defaultResult();
   }

   @Override public T newobject(final KlassInternalName klassDesc) {
      return defaultResult();
   }
}
