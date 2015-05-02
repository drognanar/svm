package com.lexicalscope.symb.vm.symb;

import static com.lexicalscope.svm.j.instruction.symbolic.symbols.SymbolMatchers.symbolEquivalentTo;
import static com.lexicalscope.svm.vm.symb.matchers.SymbStateMatchers.toModel;
import static java.lang.Math.min;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.vm.conc.junit.TestEntryPoint;
import com.lexicalscope.svm.vm.conc.junit.WithEntryPoint;
import com.lexicalscope.svm.vm.symb.junit.Fresh;
import com.lexicalscope.svm.vm.symb.junit.SymbVmRule;

public class TestCreateArrayWithSymbolicLength {
   @Rule public final SymbVmRule vm = SymbVmRule.createSymbVmRule();
   private @Fresh ISymbol symbol1;

   @TestEntryPoint public static Object[] create(final int length) {
      return new Object[length];
   }

   @TestEntryPoint public static int[] fillArrayWithSymbolicLength(final int length) {
      final int[] array = new int[length];
      for (int i = 0; i < min(4, array.length); i++) {
         array[i] = i;
      }
      return array;
   }

   @TestEntryPoint public static int reverseArrayWithSymbolicLength(final int length) {
      if(length <= 0) {
         return 0;
      }

      final int[] array = new int[length];
      for (int i = 0; i < min(array.length, 4); i++) {
         array[i] = i;
      }

      final int[] result = new int[length];
      for (int i = 0; i < min(array.length, 4); i++) {
         result[array.length - 1 - i] = array[i];
      }

      return result[0];
   }

   @Test @WithEntryPoint("create") public void createArrayWithSymbolicLength() throws Exception {
      vm.execute(symbol1);
   }

   @Test @WithEntryPoint("fillArrayWithSymbolicLength") public void fillArrayWithSymbolicLength() throws Exception {
      vm.execute(symbol1);
   }

   @Test @WithEntryPoint("reverseArrayWithSymbolicLength") public void copyBetweenArraysWithSymbolicLength() throws Exception {
      vm.execute(symbol1);

      assertThat(vm.results(), toModel(vm.feasbilityChecker()).
            has(3, symbolEquivalentTo(0)).
            has(symbolEquivalentTo(1)).
            has(symbolEquivalentTo(2)).
            has(symbolEquivalentTo(3)).
            only().
            inAnyOrder());
   }
}
