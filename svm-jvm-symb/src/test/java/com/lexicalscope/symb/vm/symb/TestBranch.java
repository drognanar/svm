package com.lexicalscope.symb.vm.symb;

import static com.lexicalscope.symb.vm.j.StateMatchers.normalTerminiationWithResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.IConstSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.MulSymbol;
import com.lexicalscope.symb.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.symb.vm.symb.junit.Fresh;
import com.lexicalscope.symb.vm.symb.junit.SymbVmRule;

public class TestBranch {
   @Rule public final SymbVmRule vm = new SymbVmRule();

   private @Fresh ISymbol symbol1;

   // with overflow bug
   @TestEntryPoint public static int abs(final int x) {
      if (x < 0) { return x * -1; }
      return x;
   }

   @Test
   public void concExecuteLeftBranch() {
      assertThat(vm.execute(-2), normalTerminiationWithResult(2));
   }

   @Test
   public void concExecuteRightBranch() {
      assertThat(vm.execute(2), normalTerminiationWithResult(2));
   }

   @Test
   public void symbExecuteBothBranches() {
      vm.execute(symbol1);
      assertThat(vm.results(), hasSize(2));
      assertThat(vm.results(), hasItem(normalTerminiationWithResult(new MulSymbol(symbol1, new IConstSymbol(-1)))));
      assertThat(vm.results(), hasItem(normalTerminiationWithResult(symbol1)));
   }
}
