package com.lexicalscope.svm.j.instruction.symbolic;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;

public class PcMatchers {
   public static Matcher<BoolSymbol> pcIs(
         final BoolSymbol symbol) {
      return equalTo(symbol);
   }
}
