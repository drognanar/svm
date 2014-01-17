package com.lexicalscope.symb.vm;

import static com.google.common.collect.Iterables.elementsEqual;
import static java.util.Objects.hash;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.lexicalscope.symb.stack.Stack;
import com.lexicalscope.symb.stack.StackFrame;
import com.lexicalscope.symb.stack.trace.SStackTrace;
import com.lexicalscope.symb.stack.trace.SStackTraceElement;
import com.lexicalscope.symb.state.SMethodName;

public class DequeStack implements Stack {
   private final Deque<StackFrame> stack;
   private Object currentThread;

   private DequeStack(final Deque<StackFrame> stack, final Object currentThread) {
      this.stack = stack;
      this.currentThread = currentThread;
   }

   public DequeStack() {
      this(new ArrayDeque<StackFrame>(), null);
   }

   public DequeStack(final StackFrame firstStackFrame) {
      this();
      push(firstStackFrame);
   }

   @Override
   public Stack popFrame(final int returnCount) {
      pushOperands(stack.pop().pop(returnCount));
      return this;
   }

   @Override public Stack push(final StackFrame stackFrame) {
      stack.push(stackFrame);
      return this;
   }

   private Stack pushOperands(final Object[] operands) {
      topFrame().pushAll(operands);
      return this;
   }

   @Override
   public StackFrame topFrame() {
      return stack.peek();
   }

   @Override
   public int size() {
      return stack.size();
   }

   @Override
   public DequeStack snapshot() {
      final ArrayDeque<StackFrame> stackCopy = new ArrayDeque<>(stack.size());
      for (final Iterator<StackFrame> iterator = stack.descendingIterator(); iterator.hasNext();) {
         stackCopy.push(iterator.next().snapshot());
      }
      assert stackCopy.size() == stack.size();
      return new DequeStack(stackCopy, currentThread);
   }

   @Override public void currentThread(final Object currentThread) {
      this.currentThread = currentThread;
   }

   @Override public Object currentThread() {
      assert currentThread != null;
      return currentThread;
   }

   @Override
   public SStackTrace trace() {
      final List<SStackTraceElement> trace = new ArrayList<>();
      for (final StackFrame stackFrame : stack) {
         final Object methodName = stackFrame.context();
         if(methodName != null) {
            trace.add(new SStackTraceElement((SMethodName) methodName));
         }
      }
      return new SStackTrace(trace);
   }

   @Override public StackFrame previousFrame() {
      final Iterator<StackFrame> iterator = stack.iterator();
      iterator.next();
      return iterator.next();
   };

   @Override
   public boolean equals(final Object obj) {
      if (obj != null && obj.getClass().equals(this.getClass())) {
         final DequeStack that = (DequeStack) obj;
         return elementsEqual(this.stack, that.stack);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return hash(stack.toArray());
   }

   @Override
   public String toString() {
      return String.format("%s", stack);
   }
}
