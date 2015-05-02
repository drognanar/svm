package com.lexicalscope.svm.j.statementBuilder;

import static com.lexicalscope.svm.vm.j.InstructionCode.synthetic;

import com.lexicalscope.svm.j.instruction.InstructionInternal;
import com.lexicalscope.svm.j.instruction.factory.AbstractInstructionSink;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource.InstructionSink;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public final class StatementBuilder {
   private final InstructionSource source;
   private Instruction insertBeforeInstruction;
   private int maxStack;
   private int maxLocals;

   private Instruction first;
   private Instruction next;

   private final InstructionSource.InstructionSink sink;

   public StatementBuilder(final InstructionSource source) {
      this.source = source;
      sink = new AbstractInstructionSink() {
         @Override public void nextInstruction(final InstructionInternal node) {
            if(next != null) {next.append(node); next = node;}
            if(first == null) {first = next = node;}
         }
      };
   }

   public StatementBuilder before(final Instruction nextInstruction) {
      this.insertBeforeInstruction = nextInstruction;
      return this;
   }

   public StatementBuilder maxLocals(final int maxLocals) {
      this.maxLocals = maxLocals;
      return this;
   }

   public StatementBuilder maxStack(final int maxStack) {
      this.maxStack = maxStack;
      return this;
   }

   public MethodBody build() {
      return new MethodBody(buildInstruction(), maxStack, maxLocals);
   }

   public Instruction buildInstruction() {
      if(insertBeforeInstruction != null) {
         if(next != null) {
            next.append(insertBeforeInstruction);
         } else {
            return insertBeforeInstruction;
         }
      }
      return first;
   }

   public StatementBuilder newObject(final KlassInternalName klassDesc) {
      source.newobject(klassDesc, sink);
      return this;
   }

   public StatementBuilder aconst_null() {
      source.aconst_null(sink);
      return this;
   }

   public StatementBuilder iconst_0() {
      source.iconst_0(sink);
      return this;
   }

   public StatementBuilder iconst(final int i) {
      source.iconst(i, sink);
      return this;
   }

   public StatementBuilder lconst(final long l) {
      source.lconst(l, sink);
      return this;
   }

   public StatementBuilder return1(final SMethodDescriptor methodName) {
      source.return1(methodName, sink);
      return this;
   }

   public StatementBuilder return2(final SMethodDescriptor methodName) {
      source.return2(methodName, sink);
      return this;
   }

   public StatementBuilder returnVoid(final SMethodDescriptor methodName) {
      source.returnvoid(methodName, sink);
      return this;
   }

   public StatementBuilder aload(final int index) {
      source.aload(index, sink);
      return this;
   }

   public StatementBuilder fload(final int index) {
      source.fload(index, sink);
      return this;
   }

   public StatementBuilder dload(final int index) {
      source.dload(index, sink);
      return this;
   }

   public StatementBuilder invokeInterface(final String klassName, final String methodName, final String desc) {
      source.invokeinterface(new AsmSMethodName(klassName, methodName, desc), sink);
      return this;
   }

   public StatementBuilder loadArg(final Object object) {
      source.loadarg(object, sink);
      return this;
   }

   public StatementBuilder createInvokeSpecial(final SMethodDescriptor sMethodName) {
      source.invokespecial(sMethodName, sink);
      return this;
   }

   public StatementBuilder createInvokeStatic(final SMethodDescriptor sMethodName) {
      source.invokestatic(sMethodName, sink);
      return this;
   }

   public StatementBuilder invokeConstructorOfClassObjects(final KlassInternalName klassName) {
      source.invokeconstructorofclassobjects(klassName, sink);
      return this;
   }

   public StatementBuilder linearOp(final Vop op) {
      sink.linearOp(op, synthetic);
      return this;
   }

   public StatementBuilder reflectionnewarray() {
      source.reflectionnewarray(sink);
      return this;
   }

   public static StatementBuilder statements(final InstructionSource instructions) {
      return new StatementBuilder(instructions);
   }

   public InstructionSink sink() {
      return sink;
   }
}
