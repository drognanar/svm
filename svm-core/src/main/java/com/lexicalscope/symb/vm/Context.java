package com.lexicalscope.symb.vm;

import java.util.List;

import com.lexicalscope.symb.heap.Allocatable;
import com.lexicalscope.symb.heap.Heap;
import com.lexicalscope.symb.stack.Stack;
import com.lexicalscope.symb.stack.StackFrame;
import com.lexicalscope.symb.state.Snapshotable;
import com.lexicalscope.symb.vm.classloader.SClass;


public class Context extends StateImpl {
   private final Vm<State> vm;
   private final Statics statics;
   private final Heap heap;
   private final Stack stack;

   public Context(
         final Vm<State> vm,
         final Statics statics,
         final Heap heap,
         final Stack stack,
         final Snapshotable<?> meta) {
      super(vm, statics, stack, heap, meta);
      this.vm = vm;
      this.statics = statics;
      this.heap = heap;
      this.stack = stack;
   }

   public Object nullPointer() {
      return heap.nullPointer();
   }

   public void push(final Object operand) {
      stackFrame().push(operand);
   }

   public Object pop() {
      return stackFrame().pop();
   }

   public Object[] pop(final int count) {
      return stackFrame().pop(count);
   }

   public Object peek() {
      return stackFrame().peek();
   }

   public Object hashCode(final Object address) {
      return heap.hashCode(address);
   }

   public void advanceTo(final InstructionNode instruction) {
      stackFrame().advance(instruction);
   }

   public Object get(final Object address, final int offset) {
      return heap.get(address, offset);
   }

   public void put(final Object address, final int offset, final Object val) {
      heap.put(address, offset, val);
   }

   public Object popDoubleWord() {
      return stackFrame().popDoubleWord();
   }

   public void pushDoubleWord(final Object val) {
      stackFrame().pushDoubleWord(val);
   }

   public InstructionNode instructionJmpTarget() {
      return instruction().jmpTarget();
   }

   public InstructionNode instructionNext() {
      return instruction().next();
   }

   public void advanceToNextInstruction() {
      advanceTo(instruction().next());
   }

   public SClass load(final String klassName) {
      return statics.load(klassName);
   }

   public Object currentThread() {
      return stack.currentThread();
   }

   public void currentThreadIs(final Object address) {
      stack.currentThread(address);
   }

   public boolean isDefined(final String klass) {
      return statics.isDefined(klass);
   }

   public SClass definePrimitiveClass(final String klassName) {
      return statics.definePrimitiveClass(klassName);
   }

   public List<SClass> defineClass(final String klassName) {
      return statics.defineClass(klassName);
   }

   public SClass classClass() {
      return statics.classClass();
   }

   public void classAt(final SClass klass, final Object classAddress) {
      statics.classAt(klass, classAddress);
   }

   public Object newObject(final Allocatable klass) {
      return heap.newObject(klass);
   }

   public void staticsAt(final SClass klass, final Object classAddress) {
      statics.staticsAt(klass, classAddress);
   }

   public StaticsMarker staticsMarker(final SClass klass) {
      return statics.staticsMarker(klass);
   }

   @Override
   public State state() {
      return this;
   }

   public StackFrame previousFrame() {
      return stack.previousFrame();
   }

   public Object whereMyClassAt(final String klassName) {
      return statics.whereMyClassAt(klassName);
   }

   public Object whereMyStaticsAt(final SClass klass) {
      return statics.whereMyStaticsAt(klass);
   }

   public void local(final int var, final Object val) {
      stackFrame().local(var, val);
   }

   public void fork(final State[] states) {
      vm.fork(states);
   }

   public void popFrame(final int returnCount) {
      stack.popFrame(returnCount);
   }

   public Object local(final int var) {
      return stackFrame().local(var);
   }

   public void pushFrame(final StackFrame stackFrame) {
      stack.push(stackFrame);
   }
}
