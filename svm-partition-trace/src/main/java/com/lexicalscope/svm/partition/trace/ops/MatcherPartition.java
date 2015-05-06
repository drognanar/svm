package com.lexicalscope.svm.partition.trace.ops;

import static com.lexicalscope.svm.j.instruction.concrete.object.PartitionTagMetaKey.PARTITION_TAG;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

class MatcherPartition {
   private final Matcher<? super CallContext> aMatcher;
   private final Matcher<? super CallContext> uMatcher;
   private final Object aPartitionTag;
   private final Object uPartitionTag;

   public MatcherPartition(
         final Matcher<? super CallContext> aMatcher,
         final Matcher<? super CallContext> uMatcher,
         final Object aPartitionTag,
         final Object uPartitionTag) {
      this.aMatcher = aMatcher;
      this.uMatcher = uMatcher;
      this.aPartitionTag = aPartitionTag;
      this.uPartitionTag = uPartitionTag;
   }

   public Object tagForConstructionOf(
         final JState ctx,
         final KlassInternalName klassDesc,
         final SMethodDescriptor targetMethod,
         final Object[] args) {
      final CallContext callContext = new JStateCallContext(ctx, klassDesc, targetMethod, args);
      Object partitionTag;
      if (aMatcher.matches(callContext)) {
         partitionTag = aPartitionTag;
      } else if (uMatcher.matches(callContext)) {
         partitionTag = uPartitionTag;
      } else {
         partitionTag = ctx.getFrameMeta(PARTITION_TAG);
      }
      return partitionTag;
   }
}