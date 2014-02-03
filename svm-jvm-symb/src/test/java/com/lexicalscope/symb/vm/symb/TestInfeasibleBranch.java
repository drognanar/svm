package com.lexicalscope.symb.vm.symb;

import static com.lexicalscope.symb.vm.j.StateMatchers.normalTerminiationWithResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.symb.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.symb.vm.symb.junit.Fresh;
import com.lexicalscope.symb.vm.symb.junit.SymbVmRule;

public class TestInfeasibleBranch {
   @Rule public final SymbVmRule vm = new SymbVmRule();
   private @Fresh ISymbol symbol1;

   @TestEntryPoint public static int infeasible(final int x) {
      final int y = x + 1;
      if(y - x < 0) {
         return 10;
      }
      return -10;
    }

   @Test
   public void concExecuteLeftBranch() {
      assertThat(vm.execute(4), normalTerminiationWithResult(-10));
   }

   @Test
   public void symbExecuteShouldSearchOnlyOneBranch() {
      vm.execute(symbol1);
      assertThat(vm.results(), hasItem(normalTerminiationWithResult(-10)));
      assertThat(vm.results(), hasSize(1));
   }
}
