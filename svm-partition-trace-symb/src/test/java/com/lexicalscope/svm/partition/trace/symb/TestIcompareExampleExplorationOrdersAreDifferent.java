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
import com.lexicalscope.svm.examples.icompare.working.InsidePartition;
import com.lexicalscope.svm.examples.icompare.working.OutsidePartition;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestIcompareExampleExplorationOrdersAreDifferent {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(InsidePartition.class), partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "callSomeMethods", "(II)I");
      vm.builder().initialState().meta(TRACE, trace().build());
   }

   private @Fresh ISymbol symbol1;
   private @Fresh ISymbol symbol2;

   @Test public void pathsExploredPairwise() throws Exception {
      vm.execute(symbol1, symbol2);

      // should pass due to different exploration orders
      // this is necessary to check that other tests that are using this
      // example are not rendered vacuous by any compiler improvements
      // which might effect branch selection order.
      assertThat(
            vm.getByMeta(tag(ExamplesOneMarker.class), TRACE),
            not(equivalentTo(vm, vm.getByMeta(tag(ExamplesTwoMarker.class), TRACE))));
   }
}
