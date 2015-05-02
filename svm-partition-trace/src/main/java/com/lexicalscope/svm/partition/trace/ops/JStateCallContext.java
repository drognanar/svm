package com.lexicalscope.svm.partition.trace.ops;

import static com.google.common.base.MoreObjects.toStringHelper;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.object.GetClassOp;
import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.partition.spec.Field;
import com.lexicalscope.svm.partition.spec.Invocation;
import com.lexicalscope.svm.partition.spec.Local;
import com.lexicalscope.svm.partition.spec.Receiver;
import com.lexicalscope.svm.partition.spec.Value;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SClass;

class JStateCallContext implements CallContext {
   private final JState ctx;
   private final KlassInternalName klassDesc;

   public JStateCallContext(final JState ctx, final KlassInternalName klassDesc) {
      this.ctx = ctx;
      this.klassDesc = klassDesc;
   }

   @Override public String callerMethodName() {
      return this.ctx.currentFrame().context().toString();
   }

   @Override public Receiver receiver() {
      return new Receiver() {
         @Override public String klass() {
            return klassDesc.string();
         }

         @Override public Field field(final String field) {
            final ObjectRef receiver = (ObjectRef) ctx.currentFrame().local(0);
            final SClass klassFromHeap = new GetClassOp().klassFromHeap(ctx, receiver);

            final int offset = klassFromHeap.fieldIndex(field);

            final Object value = ctx.get(receiver, offset);

            return new Field(){
               @Override public Object value() {
                  return value;
               }};
         }
      };
   }

   @Override public Value parameter(final String path) {
      throw new UnsupportedOperationException();
   }

   @Override public Value callerParameter(final int index) {
      return new Local(){
         @Override public Object value() {
            return ctx.currentFrame().local(index);
         }};
   }

   @Override public Value calleeParameter(final int index) {
      return new Local(){
         @Override public Object value() {
            throw new UnsupportedOperationException("we don't know the constructor params because we have to tag the object before its constructor is invoked");
         }};
   }

   @Override public Invocation previously(final String klass, final String method) {
      throw new UnsupportedOperationException();
   }

   @Override public String toString() {
      return toStringHelper(this).add("klass", klassDesc).add("methodName", callerMethodName()).toString();
   }
}
