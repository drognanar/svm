package com.lexicalscope.svm.partition.trace.ops;

import com.lexicalscope.svm.heap.Allocatable;
import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.JStateAdaptor;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;

final class TagNewInstanceWithPartitionOp implements Vop {
   private final Vop newOp;
   private final KlassInternalName klassDesc;
   private final MatcherPartition partition;

   public TagNewInstanceWithPartitionOp(
         final KlassInternalName klassDesc,
         final Vop newInstruction,
         final MatcherPartition partition) {
      this.klassDesc = klassDesc;
      this.newOp = newInstruction;
      this.partition = partition;
   }

   @Override public void eval(final JState ctx) {
      newOp.eval(new JStateAdaptor(ctx){
         @Override public ObjectRef newObject(final Allocatable klass) {
            final ObjectRef newObject = ctx.newObject(klass);
            assert ctx.get(newObject, SClass.OBJECT_TAG_OFFSET) == null;
            ctx.put(newObject, SClass.OBJECT_TAG_OFFSET, partition.tagForConstructionOf(ctx, klassDesc));
            return newObject;
         }
      });
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return newOp.query(instructionQuery);
   }

   @Override public String toString() {
      return "TAG PARTITION AT NEW";
   }
}