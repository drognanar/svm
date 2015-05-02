package com.lexicalscope.symb.vm.symb;

import static com.lexicalscope.svm.vm.j.StateMatchers.normalTerminiationWithResult;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestCell {
   public static class Cell {
      private int val;

      public int get() {
         return val;
      }

      public void set(final int val) {
         this.val = val;
      }
   }

   @TestEntryPoint public static int viaCell(final int x) {
      final Cell cell = new Cell();
      cell.set(x);
      return cell.get();
   }

   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRule();
   private @Fresh ISymbol symbol1;

   @Test
   public void symbExecuteCellNewGetSet() {
      assertThat(vm.execute(symbol1), normalTerminiationWithResult(symbol1));
   }
}
