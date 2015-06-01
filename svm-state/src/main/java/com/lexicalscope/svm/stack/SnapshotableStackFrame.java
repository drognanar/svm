package com.lexicalscope.svm.stack;

import static com.lexicalscope.svm.stack.MethodScope.DYNAMIC;
import static com.lexicalscope.svm.stack.Padding.padding;
import static java.util.Arrays.copyOf;

import com.lexicalscope.svm.metastate.HashMetaState;
import com.lexicalscope.svm.metastate.MetaKey;
import com.lexicalscope.svm.metastate.SnapshotableMetaState;
import com.lexicalscope.svm.stack.trace.SMethodName;

public final class SnapshotableStackFrame implements StackFrame {
   private final Object[] stack;
   private final int opBot; // pointer to bottom of operand stack
   private int opTop; // pointer to top of operand stack
   private final int vars = 0; // pointer to local variables

   private Object instruction; // PC
   private final SMethodName context;
   private final MethodScope scope;
   private final SnapshotableMetaState meta;

   public SnapshotableStackFrame(
         final SMethodName context,
         final MethodScope scope,
         final Object instruction,
         final int maxLocals,
         final int maxStack) {
      this(context,
           scope,
           instruction,
           new Object[maxLocals + maxStack],
           maxLocals - 1,
           maxLocals - 1,
           new HashMetaState());
   }

   private SnapshotableStackFrame(
         final SMethodName context,
         final MethodScope scope,
         final Object instruction,
         final Object[] stack,
         final int opBot,
         final int opTop,
         final SnapshotableMetaState meta) {
      assert noNulls(stack);
      this.context = context;
      this.scope = scope;
      this.instruction = instruction;
      this.stack = stack;
      this.opBot = opBot;
      this.opTop = opTop;
      this.meta = meta;
   }

   @Override
   public StackFrame advance(final Object nextInstruction) {
      instruction = nextInstruction;
      return this;
   }

   @Override
   public StackFrame push(final Object val) {
      assert val != null : this;
      assert !(val instanceof Double);
      assert !(val instanceof Long);
      assert !(val instanceof Character);
      assert !(val instanceof Short);
      assert !(val instanceof Byte);

      pushInternal(val);
      return this;
   }

   @Override
   public StackFrame pushDoubleWord(final Object val) {
      assert val != null;
      assert val instanceof Double || val instanceof Long;

      pushInternal(val);
      pushInternal(padding);
      return this;
   }

   private void pushInternal(final Object val) {
      assert val != null;
      opTop++;
      stack[opTop] = val;
   }

   @Override
   public Object pop() {
      try {
         assert stack[opTop] != null : toString();
         return stack[opTop];
      } finally {
         opTop--;
      }
   }

   @Override
   public Object popDoubleWord() {
      assert peek() instanceof Padding : this;
      pop();
      return pop();
   }

   @Override
   public Object instruction() {
      return instruction;
   }

   @Override
   public StackFrame pushAll(final Object[] args) {
      assert noNulls(args);
      System.arraycopy(args, 0, stack, opTop + 1, args.length);
      opTop += args.length;
      return this;
   }

   private boolean noNulls(final Object[] args) {
      for (final Object object : args) {
         if (object == null) {
            return true;
         }
      }
      return true;
   }

   @Override
   public Object local(final int var) {
      return stack[vars + var];
   }

   @Override
   public void local(final int var, final Object val) {
      assert val != null;
      stack[vars + var] = val;
   }

   @Override
   public SnapshotableStackFrame setLocals(final Object[] args) {
      assert noNulls(args);
      assert args.length <= stack.length - vars : String.format("asked to copy %d args into a stack of length %d", args.length, stack.length - vars);
      System.arraycopy(args, 0, stack, vars, args.length);
      return this;
   }

   @Override
   public Object[] pop(final int argCount) {
      final Object[] result = peek(argCount);
      opTop -= argCount;
      return result;
   }

   @Override
   public Object[] peek(final int argCount) {
      final Object[] result = new Object[argCount];
      System.arraycopy(stack, opTop + 1 - argCount, result, 0, argCount);
      return result;
   }

   @Override
   public Object peek() {
      return stack[opTop];
   }

   @Override public Object[] locals(final int count) {
      final Object[] result = new Object[count];
      System.arraycopy(stack, vars, result, 0, count);
      return result;
   }

   @Override
   public SnapshotableStackFrame snapshot() {
      return new SnapshotableStackFrame(
            context,
            scope,
            instruction,
            copyOf(stack, stack.length),
            opBot,
            opTop,
            meta.snapshot());
   }

   @Override
   public String toString() {
      final StringBuilder locals = new StringBuilder();
      final StringBuilder operands = new StringBuilder();
      formatStack(locals, vars, opBot);
      formatStack(operands, opBot + 1, opTop);

      return String.format("%s %s l[%s]o[%s]", instruction, context, locals, operands);
   }

   private void formatStack(final StringBuilder stackString, final int start, final int end) {
      String separator = "";
      for (int i = start; i <= end; i++) {
         stackString.append(separator);
         stackString.append(stack[i]);
         separator = ", ";
      }
   }

   @Override public SMethodName context() {
      return context;
   }

   @Override public boolean isDynamic() {
      return scope.equals(DYNAMIC);
   }

   @Override
   public final <T> T getMeta(final MetaKey<T> key) {
      return meta.get(key);
   }

   @Override
   public final <T> void setMeta(final MetaKey<T> key, final T value) {
      meta.set(key, value);
   }

   @Override public boolean containsMeta(final MetaKey<?> key) {
      return meta.contains(key);
   }

   @Override public void removeMeta(final MetaKey<?> key) {
      meta.remove(key);
   }
}
