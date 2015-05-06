package com.lexicalscope.svm.partition.trace.ops;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.InstructionQuery.MethodArguments;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SClass;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

class TagConstructorRecieverWithPartition implements Vop {
   private final SMethodDescriptor targetMethod;
   private final MethodArguments methodArguments;
   private final MatcherPartition partition;

   public TagConstructorRecieverWithPartition(
         final SMethodDescriptor targetMethod,
         final MethodArguments methodArguments,
         final MatcherPartition partition) {
      this.targetMethod = targetMethod;
      this.methodArguments = methodArguments;
      this.partition = partition;
   }

   @Override public void eval(final JState ctx) {
      final Object[] args = methodArguments.peekArgs(ctx, targetMethod);
      assert ctx.get((ObjectRef) args[0], SClass.OBJECT_TAG_OFFSET) == null;
      ctx.put(
            (ObjectRef) args[0],
            SClass.OBJECT_TAG_OFFSET,
            partition.tagForConstructionOf(ctx, targetMethod.klassName(), targetMethod, args));
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.synthetic();
   }

   @Override public String toString() {
      return "TAG PARTITION AT <init>";
   }
}
