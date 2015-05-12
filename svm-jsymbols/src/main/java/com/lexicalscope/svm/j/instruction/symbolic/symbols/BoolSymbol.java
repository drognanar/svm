package com.lexicalscope.svm.j.instruction.symbolic.symbols;



public interface BoolSymbol extends Symbol {
   BoolSymbol and(BoolSymbol conjunct);
   BoolSymbol or(BoolSymbol disjunct);
   BoolSymbol not();

   boolean isTT();
   boolean isFF();
}
