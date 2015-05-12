package com.lexicalscope.svm.partition.trace.symb.search;

import java.util.List;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.metastate.MetaFactory;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.stack.Stack;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.stack.trace.SStackTrace;
import com.lexicalscope.svm.vm.j.Instruction;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.StateTag;
import com.lexicalscope.svm.vm.j.StaticsMarker;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class FakeVmState implements JState {
   private final String string;

   public FakeVmState() {
      this("");
   }

   public FakeVmState(final String string) {
      this.string = string;
   }

   @Override public void eval() {
      // does nothing
   }

   @Override public String toString() {
      if(string.isEmpty()) {
         return super.toString();
      }
      return String.format("(state %s)", string);
   }

   @Override public JState snapshot() {
      return null;
   }

   @Override public Stack stack() {
      return null;
   }

   @Override public void pushFrame(final StackFrame stackFrame) {

   }

   @Override public void popFrame(final int returnCount) {

   }

   @Override public StackFrame previousFrame() {
      return null;
   }

   @Override public StackFrame currentFrame() {
      return null;
   }

   @Override public void advanceToNextInstruction() {

   }

   @Override public void advanceTo(final Instruction instruction) {

   }

   @Override public Instruction instructionNext() {
      return null;
   }

   @Override public Instruction instructionJmpTarget() {
      return null;
   }

   @Override public Instruction instruction() {
      return null;
   }

   @Override public Object local(final int var) {
      return null;
   }

   @Override public void local(final int var, final Object val) {

   }

   @Override public void fork(final JState[] states) {

   }

   @Override public JState[] fork() {
      return null;
   }

   @Override public void goal() {

   }

   @Override public ObjectRef whereMyStaticsAt(final SClass klass) {
      return null;
   }

   @Override public void staticsAt(final SClass klass, final ObjectRef classAddress) {

   }

   @Override public ObjectRef whereMyClassAt(final KlassInternalName klassName) {
      return null;
   }

   @Override public void classAt(final SClass klass, final ObjectRef classAddress) {

   }

   @Override public StaticsMarker staticsMarker(final SClass klass) {
      return null;
   }

   @Override public List<SClass> defineClass(final KlassInternalName klassName) {
      return null;
   }

   @Override public SClass definePrimitiveClass(final KlassInternalName klassName) {
      return null;
   }

   @Override public boolean isDefined(final KlassInternalName klass) {
      return false;
   }

   @Override public SClass loadKlassFor(final KlassInternalName klassName) {
      return null;
   }

   @Override public ObjectRef newObject(final Allocatable klass) {
      return null;
   }

   @Override public Object hashCode(final ObjectRef object) {
      return null;
   }

   @Override public ObjectRef nullPointer() {
      return null;
   }

   @Override public void put(final ObjectRef address, final int offset, final Object val) {

   }

   @Override public Object get(final ObjectRef address, final int offset) {
      return null;
   }

   @Override public SClass classClass() {
      return null;
   }

   @Override public void currentThreadIs(final Object address) {

   }

   @Override public Object currentThread() {
      return null;
   }

   @Override public void push(final Object operand) {

   }

   @Override public Object[] peek(final int count) {
      return null;
   }

   @Override public Object peek() {
      return null;
   }

   @Override public Object[] pop(final int count) {
      return null;
   }

   @Override public Object pop() {
      return null;
   }

   @Override public Object[] locals(final int count) {
      return null;
   }

   @Override public void pushDoubleWord(final Object val) {

   }

   @Override public Object popDoubleWord() {
      return null;
   }

   @Override public Object peekOperand() {
      return null;
   }

   @Override public <T> T getMeta(final MetaKey<T> key) {
      return null;
   }

   @Override public <T> void setMeta(final MetaKey<T> key, final T value) {

   }

   @Override public <T> void replaceMeta(final MetaKey<T> key, final MetaFactory<T> metaFactory) {

   }

   @Override public <T> T getFrameMeta(final MetaKey<T> key) {
      return null;
   }

   @Override public <T> void setFrameMeta(final MetaKey<T> key, final T value) {

   }

   @Override public boolean containsFrameMeta(final MetaKey<?> key) {
      return false;
   }

   @Override public void removeFrameMeta(final MetaKey<?> key) {

   }

   @Override public SStackTrace trace() {
      return null;
   }

   @Override public StateTag descendentTag() {
      return null;
   }
}
