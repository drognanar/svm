package com.lexicalscope.symb.vm;

import static com.google.common.base.Objects.equal;

import com.lexicalscope.symb.heap.Heap;
import com.lexicalscope.symb.stack.Stack;
import com.lexicalscope.symb.stack.StackFrame;
import com.lexicalscope.symb.stack.StackOp;
import com.lexicalscope.symb.stack.StackVop;
import com.lexicalscope.symb.stack.trace.SStackTrace;
import com.lexicalscope.symb.state.Snapshotable;

public class StateImpl implements State {
   private final Statics statics;
   private final Stack stack;
   private final Heap heap;
   private final Snapshotable<?> meta;

   public StateImpl(
         final Statics statics,
         final Stack stack,
         final Heap heap,
         final Snapshotable<?> meta) {
      this.statics = statics;
      this.stack = stack;
      this.heap = heap;
      this.meta = meta;
   }

   @Override
   public <T> T op(final Op<T> op) {
      return stack.query(new StackOp<T>() {
         @Override public T eval(final StackFrame top, final Stack stack) {
            return op.eval(top, stack, heap, statics);
         }
      });
   }

   @Override
   public StateImpl op(final Vop op) {
      stack.query(new StackVop() {
         @Override public void eval(final StackFrame top, final Stack stack) {
            op.eval(top, stack, heap, statics);
         }
      });
      return this;
   }

   @Override
   public void executeNextInstruction(final Vm<State> vm) {
      ((InstructionNode) stack.topFrame().instruction()).eval(vm, this);
   }

   @Override
   public StateImpl[] fork(){
      return new StateImpl[]{this.snapshot(), this.snapshot()};
   }

   @Override
   public Object getMeta() {
      return meta;
   }

   @Override public StateImpl snapshot() {
      return new StateImpl(statics.snapshot(), stack.snapshot(), heap.snapshot(), meta == null ? null : meta.snapshot());
   }

   @Override public SStackTrace trace() {
      return stack.trace();
   }

   @Override
   public String toString() {
      return String.format("stack:<%s>, heap:<%s>, meta:<%s>", stack, heap, meta);
   }

   @Override public boolean equals(final Object obj) {
      if(obj != null && obj.getClass().equals(this.getClass())) {
         final StateImpl that = (StateImpl) obj;
         return equal(that.stack, this.stack) && equal(that.heap, this.heap) && equal(that.heap, this.heap);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return stack.hashCode() ^ heap.hashCode() ^ (meta == null ? 0 : meta.hashCode());
   }
}
