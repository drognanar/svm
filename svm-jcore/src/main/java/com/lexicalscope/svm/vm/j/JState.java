package com.lexicalscope.svm.vm.j;

import java.util.List;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.metastate.MetaFactory;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.stack.Stack;
import com.lexicalscope.svm.stack.StackFrame;
import com.lexicalscope.svm.stack.trace.SStackTrace;
import com.lexicalscope.svm.state.Snapshotable;
import com.lexicalscope.svm.vm.VmState;
import com.lexicalscope.svm.vm.j.klass.SClass;

public interface JState extends Snapshotable<JState>, VmState {
   Stack stack();
   void pushFrame(StackFrame stackFrame);
   void popFrame(int returnCount);
   StackFrame previousFrame();
   StackFrame currentFrame();

   void advanceToNextInstruction();
   void advanceTo(Instruction instruction);
   Instruction instructionNext();
   Instruction instructionJmpTarget();
   Instruction instruction();

   Object local(int var);
   void local(int var, Object val);

   void fork(JState[] states);
   JState[] fork();
   void goal();

   ObjectRef whereMyStaticsAt(SClass klass);
   void staticsAt(SClass klass, ObjectRef classAddress);
   ObjectRef whereMyClassAt(KlassInternalName klassName);
   void classAt(SClass klass, ObjectRef classAddress);

   StaticsMarker staticsMarker(SClass klass);
   List<SClass> defineClass(KlassInternalName klassName);
   SClass definePrimitiveClass(KlassInternalName klassName);
   boolean isDefined(KlassInternalName klass);
   SClass loadKlassFor(KlassInternalName klassName);

   ObjectRef newObject(Allocatable klass);
   Object hashCode(ObjectRef object);
   ObjectRef nullPointer();
   void put(ObjectRef address, int offset, Object val);
   Object get(ObjectRef address, int offset);

   SClass classClass();

   void currentThreadIs(Object address);
   Object currentThread();

   void push(Object operand);
   Object[] peek(int count);
   Object peek();
   Object[] pop(int count);
   Object pop();
   Object[] locals(int count);
   void pushDoubleWord(Object val);
   Object popDoubleWord();
   Object peekOperand();

   <T> T getMeta(MetaKey<T> key);
   <T> void setMeta(MetaKey<T> key, T value);
   <T> void replaceMeta(MetaKey<T> key, MetaFactory<T> metaFactory);

   <T> T getFrameMeta(MetaKey<T> key);
   <T> void setFrameMeta(MetaKey<T> key, T value);
   boolean containsFrameMeta(MetaKey<?> key);
   void removeFrameMeta(MetaKey<?> key);

   SStackTrace trace();
   StateTag descendentTag();
}