package com.lexicalscope.svm.vm.j;

import static com.google.common.base.Objects.equal;

import java.util.List;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.Heap;
import com.lexicalscope.svm.metastate.MetaFactory;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.metastate.MetaState;
import com.lexicalscope.svm.stack.Stack;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.stack.trace.SStackTrace;
import com.lexicalscope.svm.vm.StateSearch;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class JStateImpl implements JState {
   private final Statics statics;
   private final Stack stack;
   private final Heap heap;
   private final MetaState meta;
   private final StateSearch<JState> vm;
   private final StateTag tag;

   public JStateImpl(
         final StateTag tag,
         final StateSearch<JState> vm,
         final Statics statics,
         final Stack stack,
         final Heap heap,
         final MetaState meta) {
      this.tag = tag;
      this.vm = vm;
      this.statics = statics;
      this.stack = stack;
      this.heap = heap;
      this.meta = meta;
   }

   @Override
   public final void eval() {
      instruction().eval(this);
   }

   @Override
   public final Instruction instruction() {
      return (Instruction) stackFrame().instruction();
   }

   private final StackFrame stackFrame() {
      return stack().topFrame();
   }

   @Override
   public final Stack stack() {
      return stack;
   }

   @Override
   public final JState[] fork(){
      return new JState[]{this.snapshot(), this.snapshot()};
   }

   @Override
   public final <T> T getMeta(final MetaKey<T> key) {
      return meta.get(key);
   }

   @Override
   public final <T> void setMeta(final MetaKey<T> key, final T value) {
      meta.set(key, value);
   }

   @Override public <T> void replaceMeta(final MetaKey<T> key, final MetaFactory<T> metaFactory) {
      meta.set(key, metaFactory.replacement(meta.get(key)));
   }

   @Override public final JStateImpl snapshot() {
      return new JStateImpl(tag, vm, statics.snapshot(), stack().snapshot(), heap.snapshot(), meta == null ? null : meta.snapshot());
   }

   @Override public final SStackTrace trace() {
      return stack().trace();
   }

   @Override
   public final String toString() {
      return String.format("stack:<%s>, heap:<%s>, meta:<%s>", stack(), heap, meta);
   }

   @Override public boolean equals(final Object obj) {
      if(obj != null && obj.getClass().equals(this.getClass())) {
         final JStateImpl that = (JStateImpl) obj;
         return
               equal(that.tag, this.tag) &&
               equal(that.meta, this.meta) &&
               equal(that.vm, this.vm) &&
               equal(that.statics, this.statics) &&
               equal(that.heap, this.heap) &&
               equal(that.heap, this.heap);
      }
      return false;
   }

   @Override
   public final int hashCode() {
      return stack().hashCode() ^ heap.hashCode() ^ (meta == null ? 0 : meta.hashCode());
   }

   @Override public Object peekOperand() {
      return stackFrame().peek();
   }

   @Override
   public Object nullPointer() {
      return heap.nullPointer();
   }

   @Override
   public void push(final Object operand) {
      stackFrame().push(operand);
   }

   @Override
   public Object pop() {
      return stackFrame().pop();
   }

   @Override
   public Object[] pop(final int count) {
      return stackFrame().pop(count);
   }

   @Override
   public Object peek() {
      return stackFrame().peek();
   }

   @Override public Object[] peek(final int count) {
      return stackFrame().peek(count);
   }

   @Override public Object[] locals(final int count) {
      return stackFrame().locals(count);
   }

   @Override
   public Object hashCode(final Object address) {
      return heap.hashCode(address);
   }

   @Override public void advanceTo(final Instruction instruction) {
      stackFrame().advance(instruction);
   }

   @Override
   public Object get(final Object address, final int offset) {
      return heap.get(address, offset);
   }

   @Override
   public void put(final Object address, final int offset, final Object val) {
      heap.put(address, offset, val);
   }

   @Override
   public Object popDoubleWord() {
      return stackFrame().popDoubleWord();
   }

   @Override
   public void pushDoubleWord(final Object val) {
      stackFrame().pushDoubleWord(val);
   }

   @Override
   public Instruction instructionJmpTarget() {
      return instruction().jmpTarget();
   }

   @Override
   public Instruction instructionNext() {
      return instruction().next();
   }

   @Override
   public void advanceToNextInstruction() {
      advanceTo(instruction().next());
   }

   @Override
   public SClass load(final String klassName) {
      return statics.load(klassName);
   }

   @Override
   public Object currentThread() {
      return stack.currentThread();
   }

   @Override
   public void currentThreadIs(final Object address) {
      stack.currentThread(address);
   }

   @Override
   public boolean isDefined(final String klass) {
      return statics.isDefined(klass);
   }

   @Override
   public SClass definePrimitiveClass(final String klassName) {
      return statics.definePrimitiveClass(klassName);
   }

   @Override
   public List<SClass> defineClass(final String klassName) {
      return statics.defineClass(klassName);
   }

   @Override
   public SClass classClass() {
      return statics.classClass();
   }

   @Override
   public void classAt(final SClass klass, final Object classAddress) {
      statics.classAt(klass, classAddress);
   }

   @Override
   public Object newObject(final Allocatable klass) {
      return heap.newObject(klass);
   }

   @Override
   public void staticsAt(final SClass klass, final Object classAddress) {
      statics.staticsAt(klass, classAddress);
   }

   @Override
   public StaticsMarker staticsMarker(final SClass klass) {
      return statics.staticsMarker(klass);
   }

   @Override
   public StackFrame previousFrame() {
      return stack.previousFrame();
   }

   @Override public StackFrame currentFrame() {
      return stack.currentFrame();
   }

   @Override
   public Object whereMyClassAt(final String klassName) {
      return statics.whereMyClassAt(klassName);
   }

   @Override
   public Object whereMyStaticsAt(final SClass klass) {
      return statics.whereMyStaticsAt(klass);
   }

   @Override
   public void local(final int var, final Object val) {
      stackFrame().local(var, val);
   }

   @Override
   public void fork(final JState[] states) {
      vm.fork(states);
   }

   @Override public void goal() {
      vm.goal();
   }

   @Override
   public void popFrame(final int returnCount) {
      stack.popFrame(returnCount);
   }

   @Override
   public Object local(final int var) {
      return stackFrame().local(var);
   }

   @Override
   public void pushFrame(final StackFrame stackFrame) {
      stack.push(stackFrame);
   }

   @Override public StateTag tag() {
      return tag;
   }
}
