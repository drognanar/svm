package com.lexicalscope.svm.stack;

import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.stack.trace.SMethodName;
import com.lexicalscope.svm.state.Snapshotable;

public interface StackFrame extends Snapshotable<StackFrame> {
   StackFrame advance(Object nextInstruction);
   Object instruction();

   StackFrame push(Object val);
   StackFrame pushDoubleWord(Object val);
   StackFrame pushAll(Object[] args);

   Object pop();
   Object popDoubleWord();
   Object[] pop(int argCount);
   Object[] peek(int argCount);
   Object peek();
   Object[] locals(int count);

   StackFrame setLocals(Object[] args);
   Object local(int var);
   void local(int var, Object val);

   SMethodName context();
   boolean isDynamic();

   <T> T getMeta(MetaKey<T> key);
   <T> void setMeta(MetaKey<T> key, T value);
   boolean containsMeta(MetaKey<?> key);
   void removeMeta(MetaKey<?> key);
}