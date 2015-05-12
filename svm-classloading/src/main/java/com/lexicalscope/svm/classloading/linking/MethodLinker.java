package com.lexicalscope.svm.classloading.linking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.lexicalscope.svm.classloading.MethodInstrumentor;
import com.lexicalscope.svm.j.instruction.InstructionInternal;
import com.lexicalscope.svm.j.instruction.factory.AbstractInstructionSink;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.factory.InstructionSwitch;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class MethodLinker {
   private final Multimap<AbstractInsnNode, Instruction> linked = LinkedListMultimap.create();
   private AbstractInsnNode asmInstruction;
   private final InstructionSource instructions;
   private final SMethodDescriptor methodName;
   private final MethodNode method;
   private final MethodInstrumentor instrumentor;

   public MethodLinker(
         final MethodNode method,
         final SMethodDescriptor methodName,
         final AbstractInsnNode entryPoint,
         final InstructionSource instructions,
         final MethodInstrumentor classLoader) {
      this.method = method;
      this.methodName = methodName;
      asmInstruction = entryPoint;
      this.instructions = instructions;
      this.instrumentor = classLoader;
   }

   public LinkedMethod linkBytecodeMethod() {
      final List<AbstractInsnNode> unlinked = new ArrayList<>();
      final Instruction[] prev = new Instruction[1];

      final InstructionSource.InstructionSink instructionSink = new AbstractInstructionSink() {
         @Override public void nextInstruction(final InstructionInternal node) {
            for (final AbstractInsnNode unlinkedInstruction : unlinked) {
               assert unlinkedInstruction != null;
               linked.put(unlinkedInstruction, node);
            }
            unlinked.clear();
            assert asmInstruction != null;
            linked.put(asmInstruction, node);
            if(prev[0] != null) {
               prev[0].append(node);
            }
            prev[0] = node;
         }

         @Override public void noOp() {
            unlinked.add(asmInstruction);
         }
      };

      instructions.methodentry(methodName, instructionSink);
      while(asmInstruction != null) {
         instructionFor(asmInstruction, instructionSink);
         asmInstruction = asmInstruction.getNext();
      }

      connectUpJumpsWithTargets();

      return new LinkedMethod(
            method.maxLocals,
            method.maxStack,
            instrumentor.instrument(methodName, linked.values().iterator().next()));
   }

   private void connectUpJumpsWithTargets() {
      for (final Entry<AbstractInsnNode, Instruction> entry : linked.entries()) {
         if(entry.getKey() instanceof JumpInsnNode) {
            final JumpInsnNode asmJumpInstruction = (JumpInsnNode) entry.getKey();
            final AbstractInsnNode asmInstructionAfterTargetLabel = asmJumpInstruction.label.getNext();
            assert linked.get(asmInstructionAfterTargetLabel).size() == 1;
            final Instruction jmpTarget = linked.get(asmInstructionAfterTargetLabel).iterator().next();

            assert asmInstructionAfterTargetLabel != null;
            assert jmpTarget != null : asmInstructionAfterTargetLabel;

            entry.getValue().jmpTarget(jmpTarget);
         }
      }
   }

   private void instructionFor(
         final AbstractInsnNode abstractInsnNode,
         final InstructionSource.InstructionSink sink) {

      switch (abstractInsnNode.getType()) {
         case AbstractInsnNode.LINE:
            sink.line(((LineNumberNode) abstractInsnNode).line);
         case AbstractInsnNode.FRAME:
         case AbstractInsnNode.LABEL:
            sink.noOp();
            return;
      }

      new InstructionSwitch(instructions).instructionFor(abstractInsnNode, methodName, sink);
   }
}