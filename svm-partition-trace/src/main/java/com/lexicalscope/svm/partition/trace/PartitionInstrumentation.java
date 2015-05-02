package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.svm.partition.trace.ops.CheckPartitionAtMethodEntryExit.checkPartitionAtMethodEntryExit;
import static com.lexicalscope.svm.partition.trace.ops.TrackPartitionAtNew.constructionOf;
import static com.lexicalscope.svm.vm.j.klass.SMethodDescriptorMatchers.anyMethod;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.partition.spec.CallContext;
import com.lexicalscope.svm.vm.conc.junit.VmRule;


public class PartitionInstrumentation {
   public static void instrumentPartition(
         final PartitionBuilder aPart,
         final PartitionBuilder uPart,
         final VmRule vm) {
      final Matcher<? super CallContext> aPartNewInstanceMatcher = aPart.newInstanceMatcher();
      final Matcher<? super CallContext> uPartNewInstanceMatcher = uPart.newInstanceMatcher();
      instrumentPartition(aPartNewInstanceMatcher, uPartNewInstanceMatcher, vm);
   }

   public static void instrumentPartition(
         final Matcher<? super CallContext> aPartNewInstanceMatcher,
         final Matcher<? super CallContext> uPartNewInstanceMatcher,
         final VmRule vm) {
      vm.initialStateBuilder().instrument(anyMethod(),
            constructionOf(aPartNewInstanceMatcher, uPartNewInstanceMatcher));
      vm.initialStateBuilder().instrument(anyMethod(), checkPartitionAtMethodEntryExit());
   }
}
