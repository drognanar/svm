package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.MatchersAdditional.has;
import static com.lexicalscope.svm.partition.spec.MatchersSpec.*;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;
import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static com.lexicalscope.svm.partition.trace.TraceMatchers.*;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;
import static com.lexicalscope.svm.vm.j.JavaConstants.INIT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.CombinableMatcher.both;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.svm.vm.conc.junit.VmRule;

public class TestMethodParametersCanBeUsedForPartitioning {
   @Rule public final VmRule vm = new VmRule();
   {
      instrumentPartition(
            receiverClass(ClassOutsidePartition.class),
            both(receiverClass(ClassSometimesInsidePartition.class)).
               and(calleeParameter(1, value(equalTo((Object) 2)))),
            vm);
      vm.builder().initialState().meta(TRACE, trace().build());
   }

   public static class ClassSometimesInsidePartition {
      private final int y;

      public ClassSometimesInsidePartition(final int y) { this.y = y; }

      public int myMethod(final int x){ return x*y; }
   }

   public static class ClassOutsidePartition {
      public int entry(final int x1, final int x2) {
         return new ClassSometimesInsidePartition(1).myMethod(x1) +
                new ClassSometimesInsidePartition(2).myMethod(x2);
      }
   }

   @TestEntryPoint public static void callSomeMethods() {
      new ClassOutsidePartition().entry(3, 4);
   }

   @Test public void collectArgumentsInTrace() throws Exception {
      vm.execute();

      assertThat(
            vm.result().getMeta(TRACE),
            has(methodCallOf(ClassSometimesInsidePartition.class,
                     INIT, "(I)V", any(Object.class), equalTo((Object) 2)),
                methodReturnOf(ClassSometimesInsidePartition.class,
                      INIT, "(I)V"),
                methodCallOf(ClassSometimesInsidePartition.class,
                      "myMethod", "(I)I", any(Object.class), equalTo((Object) 4)),
                methodReturnOf(ClassSometimesInsidePartition.class,
                      "myMethod", "(I)I", equalTo((Object) 8))).only().inOrder());
   }
}
