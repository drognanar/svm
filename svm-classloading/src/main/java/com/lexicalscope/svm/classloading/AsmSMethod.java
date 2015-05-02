package com.lexicalscope.svm.classloading;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.lexicalscope.svm.classloading.linking.LinkedMethod;
import com.lexicalscope.svm.classloading.linking.MethodLinker;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.klass.SMethod;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class AsmSMethod implements SMethod {
   private final SClassLoader classLoader;
   private final SMethodDescriptor methodName;
   private final MethodNode method;
   private final InstructionSource instructions;

   private Instruction entryPoint;
   private int maxLocals;
   private int maxStack;

   public AsmSMethod(
         final SClassLoader classLoader,
         final SMethodDescriptor methodName,
         final InstructionSource instructions,
         final MethodNode method) {
      this.classLoader = classLoader;
      this.methodName = methodName;
      this.instructions = instructions;
      this.method = method;
   }

   @Override
   public int maxLocals() {
      link();
      return maxLocals;
   }

   @Override
   public int maxStack() {
      link();
      return maxStack;
   }

   @Override
   public Instruction entry() {
      link();
      return entryPoint;
   }

   private void link() {
      if(entryPoint != null) {
         return;
      }

      final LinkedMethod linkedMethod;
      if((method.access & Opcodes.ACC_NATIVE) != 0) {
         linkedMethod = linkNativeMethod();
      } else {
         linkedMethod = new MethodLinker(method, methodName, getEntryPoint(), instructions, classLoader).linkBytecodeMethod();
      }
      maxLocals = linkedMethod.maxLocals();
      maxStack = linkedMethod.maxStack();
      entryPoint = linkedMethod.entryPoint();
   }

   private LinkedMethod linkNativeMethod() {
      final MethodBody resolved = classLoader.resolveNative(methodName);

      maxLocals = resolved.maxLocals();
      maxStack = resolved.maxStack();
      entryPoint = resolved.entryPoint();
      return new LinkedMethod(resolved.maxLocals(), resolved.maxStack(), resolved.entryPoint());
   }

   private AbstractInsnNode getEntryPoint() {
      return method.instructions.get(0);
   }

   @Override
   public int argSize() {
      return Type.getMethodType(method.desc).getArgumentsAndReturnSizes() >> 2;
   }

   @Override
   public SMethodDescriptor name() {
      return methodName;
   }

   @Override public String toString() {
      return name().toString();
   }
}
