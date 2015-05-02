package com.lexicalscope.svm.partition.trace.symb.search;

import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.icompare.broken.InsidePartition;
import com.lexicalscope.svm.examples.icompare.broken.OutsidePartition;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.partition.trace.symb.tree.GuidedStateSearchFactory;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestGuidedSearchFailure {
   @Rule public final ExpectedException exception = ExpectedException.none();
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(InsidePartition.class),
                          partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "callSomeMethods", "(II)I");
      vm.builder().searchWith(new GuidedStateSearchFactory(vm.feasbilityChecker()));
   }

   private @Fresh ISymbol symbol1;
   private @Fresh ISymbol symbol2;

   @Test public void pathsExploredPairwise() throws Exception {
      exception.expectMessage("unbounded");
      vm.execute(symbol1, symbol2);
   }
}
