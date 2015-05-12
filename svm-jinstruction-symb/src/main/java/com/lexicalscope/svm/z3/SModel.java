package com.lexicalscope.svm.z3;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IConstSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.Symbol;
import com.microsoft.z3.BitVecNum;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.FuncInterp;
import com.microsoft.z3.FuncInterp.Entry;
import com.microsoft.z3.Model;
import com.microsoft.z3.Z3Exception;
import com.microsoft.z3.enumerations.Z3_decl_kind;
import com.microsoft.z3.enumerations.Z3_parameter_kind;

public class SModel {
   private final Model solvedModel;
   private final Context ctx;

   public SModel(final Context ctx, final Model solvedModel) {
      this.ctx = ctx;
      this.solvedModel = solvedModel;
   }

   public Symbol interp(final Symbol symbol) {
      try {
         final Expr interpretation = solvedModel.getConstInterp(symbol.accept(new SymbolToExpr(ctx)));
         if(interpretation == null) {
            return null;
         } else if(interpretation instanceof BitVecNum) {
            return new IConstSymbol(bitVecToInt(interpretation));
         }
         return new SimplifiedSymbol(interpretation);
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to check satisfiablility", e);
      }
   }

   private int bitVecToInt(final Expr interpretation) throws Z3Exception {
      return (int) ((BitVecNum) interpretation).getLong();
   }

   public int[] funcInterp(final IArrayAndLengthSymbols arraySymbol) {
      try {
         final SymbolToExpr symbolToExpr = new SymbolToExpr(ctx);

         // an array interpretation is represented as a parameterised instance of
         // the as_array function, so some crazy stuff is required to retrieve
         // the values

         final Expr arrayExpr = symbolToExpr.toExpr(arraySymbol.getArraySymbol());
         final Expr arrayEval = solvedModel.eval(arrayExpr, false);
         final FuncDecl arrayEvalFuncDecl = arrayEval.getFuncDecl();

         assert arrayEvalFuncDecl.getDeclKind() == Z3_decl_kind.Z3_OP_AS_ARRAY;
         assert arrayEval.isApp();
         assert arrayEvalFuncDecl.getNumParameters() == 1;
         assert arrayEvalFuncDecl.getParameters()[0].getParameterKind() == Z3_parameter_kind.Z3_PARAMETER_FUNC_DECL;

         final FuncDecl arrayInterpretationFuncDecl = arrayEvalFuncDecl.getParameters()[0].getFuncDecl();

         final FuncInterp interpretation = solvedModel.getFuncInterp(arrayInterpretationFuncDecl);
         final Expr lengthInterpretation = solvedModel.getConstInterp(symbolToExpr.toExpr(arraySymbol.getLengthSymbol()));

         final int[] result = new int[bitVecToInt(lengthInterpretation)];

         final int elseValue = bitVecToInt(interpretation.getElse());
         for (int i = 0; i < result.length; i++) {
            result[i] = elseValue;
         }

         final Entry[] entries = interpretation.getEntries();
         for (final Entry entry : entries) {
            assert entry.getNumArgs() == 1;
            final int index = bitVecToInt(entry.getArgs()[0]);
            result[index] = bitVecToInt(entry.getValue());
         }
         return result;
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to check satisfiablility", e);
      }
   }
}
