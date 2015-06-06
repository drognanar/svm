package com.lexicalscope.svm.j.instruction.symbolic.symbols;

public interface SymbolVisitor<T, E extends Throwable> {
   T add(ISymbol left, ISymbol right) throws E;
   T rem(ISymbol left, ISymbol right) throws E;
   T and(ISymbol left, ISymbol right) throws E;
   T mul(ISymbol left, ISymbol right) throws E;
   T sub(ISymbol left, ISymbol right) throws E;
   T div(ISymbol left, ISymbol right) throws E;
   T neg(ISymbol value) throws E;

   T constant(int val) throws E;

   T ge(ISymbol val) throws E;
   T ge(ISymbol value1, ISymbol value2) throws E;
   T gt(ISymbol val) throws E;
   T gt(ISymbol value1, ISymbol value2) throws E;
   T le(ISymbol val) throws E;
   T le(ISymbol value1, ISymbol value2) throws E;
   T lt(ISymbol val) throws E;
   T lt(ISymbol value1, ISymbol value2) throws E;
   T ne(ISymbol val) throws E;
   T ne(ISymbol value1, ISymbol value2) throws E;
   T eq(ISymbol val) throws E;
   T eq(ISymbol value1, ISymbol value2) throws E;

   T not(BoolSymbol val) throws E;
   T and(BoolSymbol left, BoolSymbol right) throws E;
   T or(BoolSymbol left, BoolSymbol right) throws E;

   T intSymbol(String name) throws E;

   T intArraySymbol(int name) throws E;
   T intArrayZeroed() throws E;
   T iarrayStore(IArraySymbol arraySymbol, ISymbol indexSymbol, ISymbol valueSymbol) throws E;
   T iarraySelect(IArraySymbol arraySymbol, ISymbol indexSymbol) throws E;

   T tru3() throws E;
   T fals3() throws E;

   T simplified(Object simplification) throws E;
}
