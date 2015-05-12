package com.lexicalscope.svm.z3;

import static com.google.common.base.Joiner.on;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.microsoft.z3.ApplyResult;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Goal;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Tactic;
import com.microsoft.z3.Z3Exception;

public class Simplifier {
   private final Context ctx;

   public Simplifier(final Context ctx) {
      this.ctx = ctx;
   }

   public SModel powerSimplify(final BoolSymbol pc, final BoolSymbol ... exprToSolve) {
      try {
         final BoolExpr[] z3ToSolve = new BoolExpr[exprToSolve.length + 1];
         for (int i = 0; i < exprToSolve.length; i++) {
            z3ToSolve[i] = (BoolExpr) exprToSolve[i].accept(new SymbolToExpr(ctx));
         }
         z3ToSolve[exprToSolve.length] = (BoolExpr) pc.accept(new SymbolToExpr(ctx));
         return new SModel(ctx, powerSimplify(z3ToSolve));
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to create expressions", e);
      }
   }

   private Model powerSimplify(final BoolExpr ... exprToSolve) {
      try {
         final Solver solver = ctx.mkSolver();
         try {
            final ApplyResult resultOfApply = configureSolverForSimplify(solver, exprToSolve);

            final Status status = solver.check();
            if(!status.equals(Status.SATISFIABLE)) {
               throw new RuntimeException("unable to find model for " + exprToSolve);
            }

            return resultOfApply.convertModel(0, solver.getModel());
         } finally {
            solver.dispose();
         }
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to simplify " + on(", ").join(exprToSolve), e);
      }
   }

   public Status check(final BoolExpr ... exprToSolve) {
      try {
         final Solver solver = ctx.mkSolver();
         try {
            configureSolverForSat(solver, exprToSolve);
            return solver.check();
         } finally {
            solver.dispose();
         }
      } catch (final Z3Exception e) {
         throw new RuntimeException("unable to simplify " + on(", ").join(exprToSolve), e);
      }
   }

   private ApplyResult configureSolverForSimplify(final Solver solver, final BoolExpr... exprToSolve) throws Z3Exception {
      final Tactic simplify = ctx.mkTactic("simplify");
      final Tactic solveEquations = ctx.mkTactic("solve-eqs");
      final Tactic bitBlast = ctx.mkTactic("bit-blast");
      final Tactic andInvertedGraphs = ctx.mkTactic("aig");
      final Tactic propositional = ctx.mkTactic("sat");

      final Tactic tactic =
            ctx.parAndThen(simplify,
                ctx.parAndThen(solveEquations,
                      ctx.parAndThen(bitBlast,
                            ctx.parAndThen(andInvertedGraphs,
                                  propositional))));

      return applyTactic(solver, tactic, exprToSolve);
   }

   private ApplyResult configureSolverForSat(final Solver solver, final BoolExpr... exprToSolve) throws Z3Exception {
//      final Tactic bitBlast = ctx.mkTactic("bit-blast");
//      final Tactic solveEquations = ctx.mkTactic("solve-eqs");
      final Tactic propositional = ctx.mkTactic("smt");

//      final Tactic tactic = ctx.then(
//            solveEquations,
//            bitBlast,
//            propositional);

      return applyTactic(solver, propositional, exprToSolve);
   }

   private ApplyResult applyTactic(final Solver solver, final Tactic tactic, final BoolExpr... exprToSolve) throws Z3Exception {
      final Goal goal = ctx.mkGoal(true, false, false);
      goal.add(exprToSolve);

      final ApplyResult resultOfApply = tactic.apply(goal);

      for (final BoolExpr subGoalFormula : resultOfApply.getSubgoals()[0].getFormulas()) {
         solver.add(subGoalFormula);
      }
      return resultOfApply;
   }
}
