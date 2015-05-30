package com.lexicalscope.svm.z3;

import java.util.HashMap;
import java.util.Map;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArraySymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.Symbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.SymbolVisitor;
import com.microsoft.z3.ArrayExpr;
import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BitVecSort;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Z3Exception;

public class SymbolToExpr implements SymbolVisitor<Expr, Z3Exception> {
   private static final int intWidth = 32;
   private final Map<Object, Expr> cache;
   private final Context ctx;
   final BitVecSort intBvSort;
   private final BitVecExpr bv0;
   private final ArrayExpr bva0;

   public SymbolToExpr(final Context ctx) throws Z3Exception {
      this(ctx, new HashMap<Object, Expr>());
   }

   public SymbolToExpr(final Context ctx, final Map<Object, Expr> cache) throws Z3Exception {
      this.ctx = ctx;
      this.cache = cache;
      this.bv0 = constant(0);
      this.intBvSort = ctx.mkBitVecSort(intWidth);
      this.bva0 = ctx.mkConstArray(intBvSort, bv0);
   }

   @Override public BitVecExpr add(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVAdd((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public BitVecExpr and(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVAND((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public BitVecExpr rem(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVSRem((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public BitVecExpr sub(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVSub((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public BitVecExpr mul(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVMul((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public Expr neg(final ISymbol val) throws Z3Exception {
      return ctx.mkBVNeg((BitVecExpr) toExpr(val));
   }

   @Override public BitVecExpr constant(final int val) throws Z3Exception {
      return ctx.mkBV(val, intWidth);
   }

   @Override public BoolExpr ge(final ISymbol val) throws Z3Exception {
      return ctx.mkBVSGE((BitVecExpr) val.accept(this), constant(0));
   }

   @Override public BoolExpr ge(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVSGE((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public Expr lt(final ISymbol val) throws Z3Exception {
      return ctx.mkBVSLT((BitVecExpr) val.accept(this), constant(0));
   }

   @Override public BoolExpr lt(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVSLT((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public Expr gt(final ISymbol val) throws Z3Exception {
      return ctx.mkBVSGT((BitVecExpr) val.accept(this), constant(0));
   }

   @Override public BoolExpr gt(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVSGT((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public Expr le(final ISymbol val) throws Z3Exception {
      return ctx.mkBVSLE((BitVecExpr) val.accept(this), constant(0));
   }

   @Override public BoolExpr le(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkBVSLE((BitVecExpr) toExpr(left), (BitVecExpr) toExpr(right));
   }

   @Override public Expr ne(final ISymbol val) throws Z3Exception {
      return ctx.mkNot(ctx.mkEq(val.accept(this), constant(0)));
   }

   @Override public BoolExpr ne(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkNot(eq(left, right));
   }

   @Override public Expr eq(final ISymbol val) throws Z3Exception {
      return ctx.mkEq(val.accept(this), constant(0));
   }

   @Override public BoolExpr eq(final ISymbol left, final ISymbol right) throws Z3Exception {
      return ctx.mkEq(toExpr(left), toExpr(right));
   }

   @Override public Expr not(final BoolSymbol val) throws Z3Exception {
      return ctx.mkNot((BoolExpr) toExpr(val));
   }

   @Override public Expr and(final BoolSymbol left, final BoolSymbol right) throws Z3Exception {
      return ctx.mkAnd((BoolExpr) toExpr(left), (BoolExpr) toExpr(right));
   }

   @Override public Expr or(final BoolSymbol left, final BoolSymbol right) throws Z3Exception {
      return ctx.mkOr((BoolExpr) toExpr(left), (BoolExpr) toExpr(right));
   }

   @Override public Expr intSymbol(final String name) throws Z3Exception {
      if(!cache.containsKey(name)) {
         cache.put(name, ctx.mkConst(name, intBvSort));
      }
      return cache.get(name);
//      return ctx.mkConst(name, intBvSort);
   }

   @Override public BoolExpr tru3() throws Z3Exception {
      return ctx.mkTrue();
   }

   @Override public BoolExpr fals3() throws Z3Exception {
      return ctx.mkFalse();
   }

   @Override public Expr simplified(final Object simplification) throws Z3Exception {
      return (BitVecExpr) simplification;
   }

   @Override public ArrayExpr intArrayZeroed() throws Z3Exception {
      return bva0;
   }

   @Override public ArrayExpr intArraySymbol(final int name) throws Z3Exception {
      return ctx.mkArrayConst("ia" + name, intBvSort, intBvSort);
   }

   @Override public ArrayExpr iarrayStore(final IArraySymbol arraySymbol, final ISymbol indexSymbol, final ISymbol valueSymbol) throws Z3Exception {
      return ctx.mkStore((ArrayExpr) arraySymbol.accept(this), (BitVecExpr) indexSymbol.accept(this), (BitVecExpr) valueSymbol.accept(this));
   }

   @Override public Expr iarraySelect(final IArraySymbol arraySymbol, final ISymbol indexSymbol) throws Z3Exception {
      return ctx.mkSelect((ArrayExpr) arraySymbol.accept(this), indexSymbol.accept(this));
   }

   public Expr toExpr(final Symbol symbol) throws Z3Exception {
      if(!cache.containsKey(symbol)) {
         cache.put(symbol, symbol.accept(this));
      }
      return cache.get(symbol);
//      return symbol.accept(this);
   }
}
