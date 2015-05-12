package com.lexicalscope.svm.partition.trace.symb.search;

import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.isort.broken.OutsidePartition;
import com.lexicalscope.svm.examples.isort.broken.SortedList;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.search.NullGuidedSearchObserver;
import com.lexicalscope.svm.search2.PartitionViolationException;
import com.lexicalscope.svm.search2.TreeSearchFactory;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestSortedListRefactoringWorking {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(SortedList.class), partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "entryPoint", "([I)V");
      vm.builder().searchWith(new TreeSearchFactory(new NullGuidedSearchObserver(), vm.feasbilityChecker()));
   }

   private @Fresh IArrayAndLengthSymbols symbol;

   @Test @Ignore public void pathsExploredPairwise() throws Exception {
      try {
         vm.execute(symbol);
      } catch (final PartitionViolationException e) {
         final int[] arrayModel = e.violationModel().modelForBv32Array(symbol);

         System.out.println(Arrays.toString(arrayModel));

         throw e;
      }
   }
}
