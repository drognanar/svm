package com.lexicalscope.symb.z3;

import java.io.Closeable;
import java.util.HashMap;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.symb.vm.symbinstructions.Pc;
import com.lexicalscope.symb.vm.symbinstructions.symbols.IConstSymbol;
import com.lexicalscope.symb.vm.symbinstructions.symbols.ISymbol;
import com.lexicalscope.symb.vm.symbinstructions.symbols.ITerminalSymbol;
import com.lexicalscope.symb.vm.symbinstructions.symbols.Symbol;
import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BitVecNum;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class FeasibilityChecker extends TypeSafeDiagnosingMatcher<Pc> implements Closeable {
   // TODO[tim]: use z3 stack for efficency
   private final Context ctx;

   public FeasibilityChecker() {
      super(Pc.class);
      try {
         Context.ToggleWarningMessages(true);
      } catch (final Z3Exception e) {
         throw new RuntimeException("could not enable warning messages", e);
      }
      //Log.open("test.log");

      final HashMap<String, String> cfg = new HashMap<String, String>();
      cfg.put("model", "true");
      try {
         ctx = new Context(cfg);
      } catch (final Z3Exception e) {
         throw new RuntimeException("could not create context", e);
      }
   }

   public boolean checkZ3IsWorking() throws Z3Exception  {
      final IntExpr x = ctx.mkIntConst("x");
      final IntExpr y = ctx.mkIntConst("y");
      final IntExpr one = ctx.mkInt(1);
      final IntExpr two = ctx.mkInt(2);

      final ArithExpr y_plus_one = ctx.mkAdd(y, one);

      final BoolExpr c1 = ctx.mkLt(x, y_plus_one);
      final BoolExpr c2 = ctx.mkGt(x, two);

      final BoolExpr q = ctx.mkAnd(c1, c2);

      return check(q);
   }

   boolean check(final BoolExpr expr) {
      try {
         final Solver s = ctx.mkSolver();
         try {
            s.add(expr);
            return s.check().equals(Status.SATISFIABLE);
         } finally {
            s.dispose();
         }
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to check satisfiablility", e);
      }
   }

   public boolean check(final Pc pc) {
      final BoolExpr expr;
      try {
         expr = pc.accept(new PcToZ3(ctx));
      } catch (final Z3Exception e) {
         throw new RuntimeException("could not map PC to Z3: " + pc, e);
      }
      return check(expr);
   }

   /**
    * Be kind, rewind.
    */
   @Override
   public void close() {
      ctx.dispose();
   }

   public interface ISimplificationResult {
      void simplifiedToValue(int value);
      void simplified(ISymbol simplification);
   }


   public void modelForBv32Expr(final ISymbol symbol, final Pc pc, final ISimplificationResult result) {
      new SimplifyISymbolGivenPc(symbol, pc, result).eval(new Simplifier(ctx));
   }

   public void modelForInputTerminalBv32Expr(final ITerminalSymbol symbol, final Pc pc, final ISimplificationResult result) {
      new ObtainExampleForITerminalGivenPc(symbol, pc, result).eval(new Simplifier(ctx));
   }

   public void simplifyBv32Expr(final ISymbol symbol, final ISimplificationResult result) {
      try {
         simplify(symbol, symbol.accept(new SymbolToExpr(ctx)), result);
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to simplify " + symbol, e);
      }
   }

   private void simplify(final ISymbol symbol, final Expr expr, final ISimplificationResult result) {
      try {
         final Expr simplification = expr.simplify();
         if(simplification.isBVNumeral()) {
            // problem with overflow handling
            // http://stackoverflow.com/questions/20383866/z3-modeling-java-twos-complement-overflow-and-underflow-in-z3-bit-vector-addit
            result.simplifiedToValue((int) ((BitVecNum) simplification).getLong());
         } else {
            result.simplified(new SimplifiedSymbol(simplification));
         }
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to simplify " + symbol, e);
      }
   }

   @Override public void describeTo(final Description description) {
      description.appendText("feasible path condition");
   }

   @Override protected boolean matchesSafely(final Pc item, final Description mismatchDescription) {
      mismatchDescription.appendText("infeasible ").appendValue(item);
      return check(item);
   }

   Context ctx() {
      return ctx;
   }

   public Symbol modelForBv32Expr(final ISymbol operand, final Pc pc) {
      final Symbol[] result = new Symbol[1];
      modelForBv32Expr(operand, pc, new ISimplificationResult(){
         @Override public void simplifiedToValue(final int value) {
            result[0] = new IConstSymbol(value);
         }

         @Override public void simplified(final ISymbol simplification) {
            result[0] = simplification;
         }});
      return result[0];
   }

   //   This uses a bit blasting tactic...
   //
   //   public int simplifyBv32Expr(final Symbol symbol) {
   //      try {
   //         final Solver s = ctx.mkSolver();
   //         try {
   //            final Tactic simplify = ctx.mkTactic("simplify");
   //            final Tactic solveEquations = ctx.mkTactic("solve-eqs");
   //            final Tactic bitBlast = ctx.mkTactic("bit-blast");
   //            final Tactic propositional = ctx.mkTactic("sat");
   //
   //            final Tactic tactic = ctx.parAndThen(simplify, ctx.parAndThen(solveEquations, ctx.parAndThen(bitBlast, propositional)));
   //
   //            final Goal goal = ctx.mkGoal(true, false, false);
   //            goal.add(ctx.mkEq(ctx.mkBVConst("__res", 32), symbol.accept(new SymbolToExpr(ctx))));
   //
   //
   //            final ApplyResult ar = tactic.apply(goal);
   //
   //            for (final BoolExpr e : ar.getSubgoals()[0].getFormulas())
   //                s.add(e);
   //            final Status q = s.check();
   //            System.out.println("Solver says: " + q);
   //            System.out.println("Model: \n" + s.getModel());
   //            System.out.println("Converted Model: \n"
   //                    + ar.convertModel(0, s.getModel()));
   //
   //            final Expr unsimple = symbol.accept(new SymbolToExpr(ctx));
   //            System.out.println("!!!!! " + unsimple);
   //            final Expr simplified = unsimple.simplify().simplify();
   //            System.out.println("!!!!! " + simplified);
   //            return 7;
   //         } finally {
   //            s.dispose();
   //         }
   //      } catch (final Z3Exception e) {
   //         throw new RuntimeException("unable to chec satisfiablility", e);
   //      }
   //   }

   @Override public String toString() {
      return "Z3 Feasibility Checker";
   }
}
