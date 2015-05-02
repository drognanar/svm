package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.MatchersAdditional.has;
import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;
import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static com.lexicalscope.svm.partition.trace.TraceMatchers.*;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;
import static com.lexicalscope.svm.vm.j.JavaConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.svm.vm.conc.junit.VmRule;

public class TestMethodParametersCollectedByTraceInstrumentation {
   @Rule public final VmRule vm = new VmRule();
   {
      instrumentPartition(partition().ofClass(ClassInsidePartition.class), partition().ofClass(ClassOutsidePartition.class), vm);
      vm.builder().initialState().meta(TRACE, trace().build());
   }

   public static class ClassInsidePartition {
      public int myMethod(final int x){ return x*2; }
   }

   public static class ClassOutsidePartition {
      public int entry(final int x) {
         return new ClassInsidePartition().myMethod(x);
      }
   }

   @TestEntryPoint public static void callSomeMethods() {
      new ClassOutsidePartition().entry(5);
   }

   @Test public void collectArgumentsInTrace() throws Exception {
      vm.execute();

      assertThat(
            vm.result().getMeta(TRACE),
            has(methodCallOf(ClassInsidePartition.class, INIT, NOARGS_VOID_DESC),
                methodReturnOf(ClassInsidePartition.class, INIT, NOARGS_VOID_DESC),
                methodCallOf(ClassInsidePartition.class, "myMethod", "(I)I", any(Object.class), equalTo((Object) 5)),
                methodReturnOf(ClassInsidePartition.class, "myMethod", "(I)I", equalTo((Object) 10))).only().inOrder());
   }
}
