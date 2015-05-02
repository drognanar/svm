package com.lexicalscope.svm.partition.trace;

import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SFieldName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class InstructionQuerySafe<T> implements InstructionQuery<T> {
   @Override public T nativ3() {
      throw new UnsupportedOperationException();
   }

   @Override public T synthetic() {
      throw new UnsupportedOperationException();
   }

   @Override public T methodentry(final SMethodDescriptor methodName) {
      throw new UnsupportedOperationException();
   }

   @Override public T methodexit() {
      throw new UnsupportedOperationException();
   }

   @Override public T arraycopy() {
      throw new UnsupportedOperationException();
   }

   @Override public T arraylength() {
      throw new UnsupportedOperationException();
   }

   @Override public T arrayload() {
      throw new UnsupportedOperationException();
   }

   @Override public T arraystore() {
      throw new UnsupportedOperationException();
   }

   @Override public T newarray() {
      throw new UnsupportedOperationException();
   }

   @Override public T branch() {
      throw new UnsupportedOperationException();
   }

   @Override public T f2i() {
      throw new UnsupportedOperationException();
   }

   @Override public T iinc() {
      throw new UnsupportedOperationException();
   }

   @Override public T i2f() {
      throw new UnsupportedOperationException();
   }

   @Override public T i2l() {
      throw new UnsupportedOperationException();
   }

   @Override public T checkcast() {
      throw new UnsupportedOperationException();
   }

   @Override public T getstatic() {
      throw new UnsupportedOperationException();
   }

   @Override public T instance0f() {
      throw new UnsupportedOperationException();
   }

   @Override public T l2i() {
      throw new UnsupportedOperationException();
   }

   @Override public T lcmp() {
      throw new UnsupportedOperationException();
   }

   @Override public T lushr() {
      throw new UnsupportedOperationException();
   }

   @Override public T invokevirtual(final SMethodDescriptor methodName, final MethodArguments arguments) {
      throw new UnsupportedOperationException();
   }

   @Override public T invokestatic(final SMethodDescriptor methodName, final MethodArguments arguments) {
      throw new UnsupportedOperationException();
   }

   @Override public T invokespecial(final SMethodDescriptor methodName, final MethodArguments arguments) {
      throw new UnsupportedOperationException();
   }

   @Override public T invokeinterface(final SMethodDescriptor methodName, final MethodArguments arguments) {
      throw new UnsupportedOperationException();
   }

   @Override public T aconst_null() {
      throw new UnsupportedOperationException();
   }

   @Override public T getfield(final SFieldName name) {
      throw new UnsupportedOperationException();
   }

   @Override public T putfield(final SFieldName name) {
      throw new UnsupportedOperationException();
   }

   @Override public T binaryop() {
      throw new UnsupportedOperationException();
   }

   @Override public T nularyop() {
      throw new UnsupportedOperationException();
   }

   @Override public T unaryop() {
      throw new UnsupportedOperationException();
   }

   @Override public T objectpoolload() {
      throw new UnsupportedOperationException();
   }

   @Override public T stringpoolload() {
      throw new UnsupportedOperationException();
   }

   @Override public T load(final int var) {
      throw new UnsupportedOperationException();
   }

   @Override public T store(final int var) {
      throw new UnsupportedOperationException();
   }

   @Override public T dup_x1() {
      throw new UnsupportedOperationException();
   }

   @Override public T pop() {
      throw new UnsupportedOperationException();
   }

   @Override public T r3turn(final SMethodDescriptor methodName, final int returnCount) {
      throw new UnsupportedOperationException();
   }

   @Override public T dup() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifacmpeq() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifle() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifacmpne() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifeq() {
      throw new UnsupportedOperationException();
   }

   @Override public T iflt() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifne() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifnonnull() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifnull() {
      throw new UnsupportedOperationException();
   }

   @Override public T icmp() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifgt() {
      throw new UnsupportedOperationException();
   }

   @Override public T got0() {
      throw new UnsupportedOperationException();
   }

   @Override public T ifge() {
      throw new UnsupportedOperationException();
   }

   @Override public T putstatic() {
      throw new UnsupportedOperationException();
   }

   @Override public T loadarg(final Object value) {
      throw new UnsupportedOperationException();
   }

   @Override public T newobject(final KlassInternalName klassDesc) {
      throw new UnsupportedOperationException();
   }
}
