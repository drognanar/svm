package com.lexicalscope.svm.partition.trace.symb.search;

import static com.lexicalscope.svm.partition.trace.PartitionBuilder.partition;
import static com.lexicalscope.svm.partition.trace.PartitionInstrumentation.instrumentPartition;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.examples.ExamplesOneMarker;
import com.lexicalscope.svm.examples.ExamplesTwoMarker;
import com.lexicalscope.svm.examples.icompare.broken.InsidePartition;
import com.lexicalscope.svm.examples.icompare.broken.OutsidePartition;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.search.NullGuidedSearchObserver;
import com.lexicalscope.svm.search2.PartitionViolationException;
import com.lexicalscope.svm.search2.TreeSearchFactory;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;
import com.lexicalscope.svm.z3.ViolationModel;

public class TestGuidedSearchFailure {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRuleLoadingFrom(ExamplesOneMarker.class, ExamplesTwoMarker.class);
   {
      instrumentPartition(partition().ofClass(InsidePartition.class),
                          partition().ofClass(OutsidePartition.class), vm);
      vm.entryPoint(OutsidePartition.class, "callSomeMethods", "(II)I");
      vm.builder().searchWith(new TreeSearchFactory(new NullGuidedSearchObserver(), vm.feasbilityChecker()));
   }

   private @Fresh ISymbol symbol1;
   private @Fresh ISymbol symbol2;

   @Test public void pathsExploredPairwise() throws Exception {
      try {
         vm.execute(symbol1, symbol2);
         Assert.fail("should have detected a violation");
      } catch (final PartitionViolationException e) {
         final ViolationModel model = e.violationModel();
         final int xVal = model.intModelForBv32Expr(symbol1);
         final int yVal = model.intModelForBv32Expr(symbol2);
         assertThat(xVal, equalTo(yVal));
      }
   }
}
