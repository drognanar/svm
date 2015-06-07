package com.lexicalscope.svm.stack;

import static com.google.common.collect.Iterables.elementsEqual;
import static java.util.Objects.hash;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.lexicalscope.svm.stack.trace.SMethodName;
import com.lexicalscope.svm.stack.trace.SStackTrace;
import com.lexicalscope.svm.stack.trace.SStackTraceElement;

public class DequeStack implements Stack {
   private DequeStackFrame stack;
   private Object currentThread;

   private DequeStack(final DequeStackFrame stack, final Object currentThread) {
      this.stack = stack;
      this.currentThread = currentThread;
   }

   public DequeStack() {
      this(DequeStackFrame.topFrame(), null);
   }

   public DequeStack(final StackFrame firstStackFrame) {
      this();
      push(firstStackFrame);
   }

   @Override
   public Stack popFrame(final int returnCount) {
      pushOperands(pop().pop(returnCount));
      return this;
   }

   @Override public Stack push(final StackFrame stackFrame) {
      this.stack = stack.push(stackFrame);
      return this;
   }

   public StackFrame pop() {
      DequeStackFrame oldStackFrame = this.stack;
      this.stack = this.stack.pop();
      return oldStackFrame.getFrame();
   }

   private Stack pushOperands(final Object[] operands) {
      topFrame().pushAll(operands);
      return this;
   }

   @Override
   public StackFrame topFrame() {
      return stack.getFrame();
   }

   @Override
   public int size() {
      return stack.size();
   }

   @Override
   public DequeStack snapshot() {
      DequeStackFrame stackCopy = stack.snapshot();
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
      return stack.getPreviousFrame().getFrame();
   }

   @Override public StackFrame currentFrame() {
      return stack.getFrame();
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

   public static class DequeStackFrame implements Iterable<StackFrame> {
      /**
       * Previous frame on the stack.
       */
      private DequeStackFrame previousFrame = null;

      /**
       * Frame.
       */
      private StackFrame frame;

      /**
       * Size of the stack frame.
       */
      private int size;

      /**
       * Returns whether a StackFrame is shared between multiple JState instances.
       */
      private boolean shared = false;

      public DequeStackFrame(StackFrame frame, DequeStackFrame previousFrame, int size) {
         this.frame = frame;
         this.previousFrame = previousFrame;
         this.size = size;
      }

      public static DequeStackFrame topFrame() {
         return new DequeStackFrame(null, null, 0);
      }

      public DequeStackFrame snapshot() {
         previousFrame.shared = true;
         return new DequeStackFrame(frame.snapshot(), previousFrame, size);
      }

      public DequeStackFrame pop() {
         DequeStackFrame previous = previousFrame;
         return previous.shared ? previous.snapshot() : previous;
      }

      public DequeStackFrame push(StackFrame frame) {
         return new DequeStackFrame(frame, this, size + 1);
      }

      public StackFrame getFrame() {
         return frame;
      }

      public DequeStackFrame getPreviousFrame() {
         return previousFrame;
      }

      public int size() {
         return size;
      }

      public StackFrame[] toArray() {
         StackFrame[] arr = new StackFrame[size];
         DequeStackFrame frame = this;
         for (int i = 0; i < size; i++) {
            arr[i] = frame.getFrame();
            frame = frame.previousFrame;
         }
         return arr;
      }

      @Override
      public String toString() {
         return Arrays.toString(toArray());
      }

      @Override
      public Iterator<StackFrame> iterator() {
         return Arrays.asList(toArray()).iterator();
      }
   }
}
