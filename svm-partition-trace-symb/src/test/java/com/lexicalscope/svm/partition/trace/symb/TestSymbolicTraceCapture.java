package com.lexicalscope.svm.partition.trace.symb;


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

import com.lexicalscope.svm.j.instruction.symbolic.symbols.IMulSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestSymbolicTraceCapture {

   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRule();
   {
      instrumentPartition(partition().ofClass(InsidePartition.class), partition().ofClass(OutsidePartition.class), vm);
      vm.builder().initialState().meta(TRACE, trace().build());
   }

   private @Fresh ISymbol symbol1;
   private @Fresh ISymbol symbol2;

   public static class InsidePartition {
      public int multiply(final int x, final int y){
         return x * y;
      }
   }

   public static class OutsidePartition {
      public int entry(final int x, final int y) {
         return new InsidePartition().multiply(x, y);
      }
   }

   @TestEntryPoint public static int callSomeMethods(final int x, final int y) {
      return new OutsidePartition().entry(x, y);
   }

   @Test public void collectSymbolicTrace() throws Exception {
      vm.execute(symbol1, symbol2);

      assertThat(
            vm.result().getMeta(TRACE),
            has(methodCallOf(InsidePartition.class, INIT, NOARGS_VOID_DESC),
                methodReturnOf(InsidePartition.class, INIT, NOARGS_VOID_DESC),
                methodCallOf(InsidePartition.class, "multiply", "(II)I", any(Object.class), equalTo(symbol1), equalTo(symbol2)),
                methodReturnOf(InsidePartition.class, "multiply", "(II)I", equalTo(new IMulSymbol(symbol1, symbol2)))).only().inOrder());
   }
}
