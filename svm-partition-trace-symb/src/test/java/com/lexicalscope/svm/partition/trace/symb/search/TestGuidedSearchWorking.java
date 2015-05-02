package com.lexicalscope.svm.partition.trace.symb.search;

import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.icompare.working.InsidePartition;
import com.lexicalscope.svm.examples.icompare.working.OutsidePartition;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.partition.trace.symb.tree.GuidedStateSearchFactory;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestGuidedSearchWorking {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(InsidePartition.class), partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "callSomeMethods", "(II)I");
      vm.builder().searchWith(new GuidedStateSearchFactory(vm.feasbilityChecker()));
   }

   private @Fresh ISymbol symbol1;
   private @Fresh ISymbol symbol2;

   @Test public void pathsExploredPairwise() throws Exception {
      vm.execute(symbol1, symbol2);
   }
}
