package com.lexicalscope.svm.partition.trace.symb.search;

import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.isort.broken.OutsidePartition;
import com.lexicalscope.svm.examples.isort.broken.SortedList;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySymbolPair;
import com.lexicalscope.svm.partition.trace.symb.tree.GuidedStateSearchFactory;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestSortedListRefactoringWorking {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(SortedList.class), partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "entryPoint", "([I)V");
      vm.builder().searchWith(new GuidedStateSearchFactory(vm.feasbilityChecker()));
   }

   private @Fresh IArraySymbolPair symbol;

   @Test public void pathsExploredPairwise() throws Exception {
      vm.execute(symbol);
   }
}
