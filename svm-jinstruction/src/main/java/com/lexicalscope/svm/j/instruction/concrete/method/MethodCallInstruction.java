package com.lexicalscope.svm.j.instruction.concrete.method;

import static com.lexicalscope.svm.vm.j.InstructionCode.*;

import com.lexicalscope.svm.j.instruction.MethodCallVop;
import com.lexicalscope.svm.j.instruction.concrete.klass.LoadingInstruction;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.stack.MethodScope;
import com.lexicalscope.svm.stack.SnapshotableStackFrame;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.stack.trace.SMethodName;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.MethodResolver;
import com.lexicalscope.svm.vm.j.State;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SMethod;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class MethodCallInstruction {
   private static final class Resolution {
      public Resolution(final String receiverKlass, final SMethod sMethod) {
         this.receiverKlass = receiverKlass;
         this.method = sMethod;
      }

      public Resolution(final SMethod sMethod) {
         this(sMethod.name().klassName(), sMethod);
      }

      final String receiverKlass;
      final SMethod method;
   }

   private interface MethodInvokation {
      Object[] args(State ctx, SMethodDescriptor targetMethod);

      String name();

      Resolution resolveMethod(Object[] args, SMethodDescriptor sMethodName, State ctx);

      MethodScope scope();

      <T> T query(SMethodDescriptor methodName, InstructionQuery<T> instructionQuery);
   }

   private static class VirtualMethodInvokation implements MethodInvokation {
      @Override public Object[] args(final State ctx, final SMethodDescriptor targetMethod) {
         return ctx.pop(targetMethod.argSize());
      }

      @Override public String name() {
         return "INVOKEVIRTUAL";
      }

      @Override public Resolution resolveMethod(final Object[] args, final SMethodDescriptor sMethodName, final State ctx) {
         return resolveVirtualMethod(args, sMethodName, ctx);
      }

      @Override public MethodScope scope() {
         return MethodScope.DYNAMIC;
      }

      @Override public <T> T query(final SMethodDescriptor methodName, final InstructionQuery<T> instructionQuery) {
         return instructionQuery.invokevirtual(methodName);
      }
   }

   private static class InterfaceMethodInvokation implements MethodInvokation {
      @Override public Object[] args(final State ctx, final SMethodDescriptor targetMethod) {
         return ctx.pop(targetMethod.argSize());
      }

      @Override public String name() {
         return "INVOKEINTERFACE";
      }

      @Override public Resolution resolveMethod(final Object[] args, final SMethodDescriptor sMethodName, final State ctx) {
         return resolveVirtualMethod(args, sMethodName, ctx);
      }

      @Override public MethodScope scope() {
         return MethodScope.DYNAMIC;
      }

      @Override public <T> T query(final SMethodDescriptor methodName, final InstructionQuery<T> instructionQuery) {
         return instructionQuery.invokeinterface(methodName);
      }
   }

   private static Resolution resolveVirtualMethod(final Object[] args, final SMethodDescriptor sMethodName, final State ctx) {
      final MethodResolver receiverKlass = receiver(args, sMethodName, ctx);
      return new Resolution(receiverKlass.virtualMethod(sMethodName));
   }

   private static MethodResolver receiver(final Object[] args, final SMethodName sMethodName, final State ctx) {
      final Object receiver = ctx.get(args[0], SClass.OBJECT_MARKER_OFFSET);
      assert receiver != null : sMethodName;
      assert receiver instanceof MethodResolver : "no " + sMethodName + " in " + receiver;
      return (MethodResolver) receiver;
   }

   private static class SpecialMethodInvokation implements MethodInvokation {
      @Override public Object[] args(final State ctx, final SMethodDescriptor targetMethod) {
         return ctx.pop(targetMethod.argSize());
      }

      @Override public String name() {
         return "INVOKESPECIAL";
      }

      @Override public Resolution resolveMethod(final Object[] args, final SMethodDescriptor sMethodName, final State ctx) {
         return new Resolution(ctx.load(sMethodName.klassName()).declaredMethod(sMethodName));
      }

      @Override public MethodScope scope() {
         return MethodScope.DYNAMIC;
      }

      @Override public <T> T query(final SMethodDescriptor methodName, final InstructionQuery<T> instructionQuery) {
         return instructionQuery.invokespecial(methodName);
      }
   }

   private static class ClassDefaultConstructorMethodInvokation implements MethodInvokation {
      private final String klassName;

      public ClassDefaultConstructorMethodInvokation(final String klassName) {
         this.klassName = klassName;
      }

      @Override public Object[] args(
            final State ctx,
            final SMethodDescriptor targetMethod) {
         return new Object[]{ctx.whereMyClassAt(klassName)};
      }

      @Override public String name() {
         return "INVOKECLASSCONSTRUCTOR";
      }

      @Override public Resolution resolveMethod(final Object[] args, final SMethodDescriptor sMethodName, final State ctx) {
         final MethodResolver receiver = receiver(args, sMethodName, ctx);
         return new Resolution(receiver.declaredMethod(sMethodName));
      }

      @Override public MethodScope scope() {
         return MethodScope.DYNAMIC;
      }

      @Override public <T> T query(final SMethodDescriptor methodName, final InstructionQuery<T> instructionQuery) {
         return instructionQuery.synthetic();
      }
   }

   private static class StaticMethodInvokation implements MethodInvokation {
      @Override public Object[] args(final State ctx, final SMethodDescriptor targetMethod) {
         return ctx.pop(targetMethod.argSize() - 1);
      }

      @Override public String name() {
         return "INVOKESTATIC";
      }

      @Override public Resolution resolveMethod(final Object[] args, final SMethodDescriptor sMethodName, final State ctx) {
         return new Resolution(ctx.load(sMethodName.klassName()).declaredMethod(sMethodName));
      }

      @Override public MethodScope scope() {
         return MethodScope.STATIC;
      }

      @Override public <T> T query(final SMethodDescriptor methodName, final InstructionQuery<T> instructionQuery) {
         return instructionQuery.invokestatic(methodName);
      }
   }

   private static final class MethodCallOp  implements MethodCallVop {
      private final MethodInvokation methodInvokation;
      private final SMethodDescriptor sMethodName;

      public MethodCallOp(final SMethodDescriptor sMethodName, final MethodInvokation methodInvokation) {
         this.sMethodName = sMethodName;
         this.methodInvokation = methodInvokation;
      }

      @Override public SMethodDescriptor getMethodName() {
         return sMethodName;
      }

      @Override
      public String toString() {
         return String.format("%s %s", methodInvokation.name(), sMethodName);
      }

      @Override public void eval(final State ctx) {
         final Object[] args = methodInvokation.args(ctx, sMethodName);

         final Resolution resolution = methodInvokation.resolveMethod(args, sMethodName, ctx);
         // TODO[tim]: virtual does not resolve overridden methods
         final SMethod targetMethod = resolution.method;
         final StackFrame newStackFrame = new SnapshotableStackFrame(targetMethod.name(), methodInvokation.scope(), targetMethod.entry(), targetMethod.maxLocals(), targetMethod.maxStack());
         ctx.pushFrame(newStackFrame.setLocals(args));
      }

      @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
         return methodInvokation.query(sMethodName, instructionQuery);
      }
   }

   public static void invokevirtual(final SMethodDescriptor name, final InstructionSource.InstructionSink sink) {
      sink.linearOp(new MethodCallOp(name, new VirtualMethodInvokation()), invokevirtual);
   }

   public static void invokeinterface(final SMethodDescriptor sMethodName, final InstructionSource.InstructionSink sink) {
      sink.linearOp(new MethodCallOp(sMethodName, new InterfaceMethodInvokation()), invokeinterface);
   }

   public static void invokespecial(final SMethodDescriptor sMethodName, final InstructionSource.InstructionSink sink) {
      sink.linearOp(new MethodCallOp(sMethodName, new SpecialMethodInvokation()), invokespecial);
   }

   public static void invokeconstructorofclassobjects(final String klassName, final InstructionSource.InstructionSink sink) {
      sink.linearOp(new MethodCallOp(JavaConstants.CLASS_CLASS_DEFAULT_CONSTRUCTOR, new ClassDefaultConstructorMethodInvokation(klassName)), invokeconstructorofclassobjects);
   }

   public static void invokestatic(final SMethodDescriptor sMethodName, final InstructionSource.InstructionSink sink, final InstructionSource instructions) {
      sink.nextOp(new LoadingInstruction(sMethodName.klassName(), new MethodCallOp(sMethodName, new StaticMethodInvokation()), instructions), invokestatic);
   }
}
