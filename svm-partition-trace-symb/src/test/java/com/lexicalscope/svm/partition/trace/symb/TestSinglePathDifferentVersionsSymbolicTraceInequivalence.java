package com.lexicalscope.svm.partition.trace.symb;

import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;
import static com.lexicalscope.svm.partition.trace.TraceBuilder.trace;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;
import static com.lexicalscope.svm.partition.trace.symb.SymbolicTraceMatchers.equivalentTo;
import static com.lexicalscope.svm.vm.conc.junit.ClassStateTag.tag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.doubler.broken.InsidePartition;
import com.lexicalscope.svm.examples.doubler.broken.OutsidePartition;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestSinglePathDifferentVersionsSymbolicTraceInequivalence {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(InsidePartition.class), partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "callSomeMethods", "(I)I");
      vm.builder().initialState().meta(TRACE, trace().build());
   }

   private @Fresh ISymbol symbol1;

   @Test public void traceFromNonEquivalentVersionsIsNotEquivalent() throws Exception {
      vm.execute(symbol1);

      assertThat(
            vm.getMeta(tag(ExamplesOneMarker.class), TRACE),
            not(equivalentTo(vm, vm.getMeta(tag(ExamplesTwoMarker.class), TRACE))));
   }
}
