package com.lexicalscope.svm.vm.j;

import java.util.List;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.metastate.MetaFactory;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.stack.Stack;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.stack.trace.SStackTrace;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class JStateAdaptor implements JState {
   private final JState delegate;

   public JStateAdaptor(final JState delegate) {
      this.delegate = delegate;
   }

   @Override
   public void eval() {
      delegate.eval();
   }

   @Override
   public JState snapshot() {
      return delegate.snapshot();
   }

   @Override
   public Stack stack() {
      return delegate.stack();
   }

   @Override
   public void pushFrame(final StackFrame stackFrame) {
      delegate.pushFrame(stackFrame);
   }

   @Override
   public void popFrame(final int returnCount) {
      delegate.popFrame(returnCount);
   }

   @Override
   public StackFrame previousFrame() {
      return delegate.previousFrame();
   }

   @Override
   public StackFrame currentFrame() {
      return delegate.currentFrame();
   }

   @Override
   public void complete() {
   }

   @Override
   public void advanceToNextInstruction() {
      delegate.advanceToNextInstruction();
   }

   @Override
   public void advanceTo(final Instruction instruction) {
      delegate.advanceTo(instruction);
   }

   @Override
   public Instruction instructionNext() {
      return delegate.instructionNext();
   }

   @Override
   public Instruction instructionJmpTarget() {
      return delegate.instructionJmpTarget();
   }

   @Override
   public Instruction instruction() {
      return delegate.instruction();
   }

   @Override
   public Object local(final int var) {
      return delegate.local(var);
   }

   @Override
   public void local(final int var, final Object val) {
      delegate.local(var, val);
   }

   @Override
   public void fork(final JState[] states) {
      delegate.fork(states);
   }

   @Override
   public void forkDisjoined(final JState[] states) {
      delegate.forkDisjoined(states);
   }

   @Override
   public JState[] fork() {
      return delegate.fork();
   }

   @Override
   public void goal() {
      delegate.goal();
   }

   @Override
   public ObjectRef whereMyStaticsAt(final SClass klass) {
      return delegate.whereMyStaticsAt(klass);
   }

   @Override
   public void staticsAt(final SClass klass, final ObjectRef classAddress) {
      delegate.staticsAt(klass, classAddress);
   }

   @Override
   public ObjectRef whereMyClassAt(final KlassInternalName klassName) {
      return delegate.whereMyClassAt(klassName);
   }

   @Override
   public void classAt(final SClass klass, final ObjectRef classAddress) {
      delegate.classAt(klass, classAddress);
   }

   @Override
   public StaticsMarker staticsMarker(final SClass klass) {
      return delegate.staticsMarker(klass);
   }

   @Override
   public List<SClass> defineClass(final KlassInternalName klassName) {
      return delegate.defineClass(klassName);
   }

   @Override
   public SClass definePrimitiveClass(final KlassInternalName klassName) {
      return delegate.definePrimitiveClass(klassName);
   }

   @Override
   public boolean isDefined(final KlassInternalName klass) {
      return delegate.isDefined(klass);
   }

   @Override
   public SClass loadKlassFor(final KlassInternalName klassName) {
      return delegate.loadKlassFor(klassName);
   }

   @Override public ObjectRef newObject(final Allocatable klass) {
      return delegate.newObject(klass);
   }

   @Override
   public Object hashCode(final ObjectRef object) {
      return delegate.hashCode(object);
   }

   @Override
   public ObjectRef nullPointer() {
      return delegate.nullPointer();
   }

   @Override
   public void put(final ObjectRef address, final int offset, final Object val) {
      delegate.put(address, offset, val);
   }

   @Override
   public Object get(final ObjectRef address, final int offset) {
      return delegate.get(address, offset);
   }

   @Override
   public SClass classClass() {
      return delegate.classClass();
   }

   @Override
   public void currentThreadIs(final Object address) {
      delegate.currentThreadIs(address);
   }

   @Override
   public Object currentThread() {
      return delegate.currentThread();
   }

   @Override
   public void push(final Object operand) {
      delegate.push(operand);
   }

   @Override
   public Object[] peek(final int count) {
      return delegate.peek(count);
   }

   @Override
   public Object peek() {
      return delegate.peek();
   }

   @Override
   public Object[] pop(final int count) {
      return delegate.pop(count);
   }

   @Override
   public Object pop() {
      return delegate.pop();
   }

   @Override
   public Object[] locals(final int count) {
      return delegate.locals(count);
   }

   @Override
   public void pushDoubleWord(final Object val) {
      delegate.pushDoubleWord(val);
   }

   @Override
   public Object popDoubleWord() {
      return delegate.popDoubleWord();
   }

   @Override
   public Object peekOperand() {
      return delegate.peekOperand();
   }

   @Override
   public <T> T getMeta(final MetaKey<T> key) {
      return delegate.getMeta(key);
   }

   @Override
   public <T> void setMeta(final MetaKey<T> key, final T value) {
      delegate.setMeta(key, value);
   }

   @Override
   public <T> void replaceMeta(final MetaKey<T> key, final MetaFactory<T> metaFactory) {
      delegate.replaceMeta(key, metaFactory);
   }

   @Override
   public <T> T getFrameMeta(final MetaKey<T> key) {
      return delegate.getFrameMeta(key);
   }

   @Override
   public <T> void setFrameMeta(final MetaKey<T> key, final T value) {
      delegate.setFrameMeta(key, value);
   }

   @Override
   public boolean containsFrameMeta(final MetaKey<?> key) {
      return delegate.containsFrameMeta(key);
   }

   @Override
   public void removeFrameMeta(final MetaKey<?> key) {
      delegate.removeFrameMeta(key);
   }

   @Override
   public SStackTrace trace() {
      return delegate.trace();
   }

   @Override
   public StateTag descendentTag() {
      return delegate.descendentTag();
   }

   @Override public String toString() {
      return delegate.toString();
   }
}
