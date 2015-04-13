package com.lexicalscope.svm.partition.trace.ops;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.concrete.object.GetClassOp;
import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.partition.spec.Field;
import com.lexicalscope.svm.partition.spec.Invocation;
import com.lexicalscope.svm.partition.spec.Receiver;
import com.lexicalscope.svm.partition.spec.Value;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.klass.SClass;

public class JStateCallContext implements CallContext {
   private final JState ctx;
   private final String klassDesc;

   public JStateCallContext(final JState ctx, final String klassDesc) {
      this.ctx = ctx;
      this.klassDesc = klassDesc;
   }

   @Override public String methodName() {
      return this.ctx.currentFrame().context().toString();
   }

   @Override public Receiver receiver() {
      return new Receiver() {
         @Override public String klass() {
            return klassDesc;
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

   @Override public Invocation previously(final String klass, final String method) {
      throw new UnsupportedOperationException();
   }
}
