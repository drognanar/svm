package com.lexicalscope.symb.vm.instructions;

import org.objectweb.asm.tree.MethodInsnNode;

import com.lexicalscope.symb.vm.Instruction;
import com.lexicalscope.symb.vm.InstructionTransform;
import com.lexicalscope.symb.vm.Stack;
import com.lexicalscope.symb.vm.State;
import com.lexicalscope.symb.vm.Vm;
import com.lexicalscope.symb.vm.classloader.SClassLoader;
import com.lexicalscope.symb.vm.classloader.SMethod;
import com.lexicalscope.symb.vm.instructions.ops.StackOp;

final class InvokeVirtual implements InstructionTransform {
   private final MethodInsnNode methodInsnNode;

   InvokeVirtual(final MethodInsnNode methodInsnNode) {
      this.methodInsnNode = methodInsnNode;
   }

   @Override
   public void eval(final SClassLoader cl, final Vm vm, final State state, final Instruction instruction) {
      final SMethod targetMethod = cl.loadMethod(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);

      // TODO[tim]: resolve overridden methods

      state.op(new StackOp<Void>() {
         @Override
         public Void eval(final Stack stack) {
            stack.pushFrame(instruction.next(), targetMethod, targetMethod.argSize());
            return null;
         }
      });
   }

   @Override
   public String toString() {
      return String.format("INVOKESPECIAL %s", methodInsnNode.desc);
   }
}