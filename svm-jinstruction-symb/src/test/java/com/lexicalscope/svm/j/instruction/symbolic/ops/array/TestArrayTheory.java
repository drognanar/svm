package com.lexicalscope.svm.j.instruction.symbolic.ops.array;


import static com.lexicalscope.svm.j.instruction.symbolic.pc.PcBuilder.icmpEq;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.junit.junitautocloseable.AutoCloseRule;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IConstSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.StateBuilder;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;
import com.lexicalscope.svm.z3.FeasibilityChecker;

public class TestArrayTheory {
   @Rule public AutoCloseRule autoCloseRule = new AutoCloseRule();
   private final FeasibilityChecker feasbilityChecker = new FeasibilityChecker();

   private final StateBuilder state = new StateBuilder();
   private final BoolSymbol pc = TrueSymbol.TT;

   @Test public void storeAndSelectAtSymbolicIndex() throws Exception {
      final ITerminalSymbol indexSymbol = state.intSymbol();
      final IConstSymbol valueSymbol = state.intConst(7);
      final IArraySymbol storedArraySymbol = state.arrayStore(state.iarrayZeroed(), indexSymbol, valueSymbol);

      assertThat(
            pc.and(icmpEq(valueSymbol, state.arraySelect(storedArraySymbol, indexSymbol))),
            feasbilityChecker);
   }

   @Test public void storeAndSelectAtPossiblyDifferentSymbolicIndex() throws Exception {
      final ITerminalSymbol storeIndexSymbol = state.intSymbol();
      final ITerminalSymbol selectIndexSymbol = state.intSymbol();
      final IConstSymbol valueSymbol = state.intConst(7);
      final IArraySymbol storedArraySymbol = state.arrayStore(state.iarrayZeroed(), storeIndexSymbol, valueSymbol);

      assertThat(
            pc.and(icmpEq(valueSymbol, state.arraySelect(storedArraySymbol, selectIndexSymbol))),
            feasbilityChecker);
   }

   @Test public void storeAndSelectAtDefinitelyDifferentSymbolicIndex() throws Exception {
      final ITerminalSymbol storeIndexSymbol = state.intSymbol();
      final ISymbol selectIndexSymbol = state.iadd(storeIndexSymbol, state.intConst(1));
      final IConstSymbol valueSymbol = state.intConst(7);
      final IArraySymbol storedArraySymbol = state.arrayStore(state.iarrayZeroed(), storeIndexSymbol, valueSymbol);

      assertThat(
            pc.and(icmpEq(valueSymbol, state.arraySelect(storedArraySymbol, selectIndexSymbol))),
            not(feasbilityChecker));
   }
}
