package com.lexicalscope.svm.j.instruction.symbolic;

import static com.lexicalscope.svm.j.instruction.symbolic.PcMetaKey.PC;

import com.lexicalscope.svm.j.instruction.symbolic.predicates.BinarySBranchOp;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.BinarySBranchStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.EqStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.GeStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.GtStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.ICmpEqStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.ICmpGeStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.ICmpGtStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.ICmpLeStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.ICmpLtStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.ICmpNeStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.LeStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.LtStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.NeStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.SBranchStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.UnarySBranchOp;
import com.lexicalscope.svm.j.instruction.symbolic.predicates.UnarySBranchStrategy;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.z3.FeasibilityChecker;

final class SBranchInstruction implements Vop {
   private final SBranchStrategy branchStrategy;
   private final FeasibilityChecker feasibilityChecker;

   SBranchInstruction(
         final SBranchStrategy branchStrategy,
         final FeasibilityChecker feasibilityChecker) {
      this.branchStrategy = branchStrategy;
      this.feasibilityChecker = feasibilityChecker;
   }

   @Override public void eval(final JState ctx) {
      final BoolSymbol branchPredicateSymbol = branchStrategy.evaluateBranchConditonAsSymbol(ctx);

      final JState jumpState;
      final JState nojumpState;

      final BoolSymbol pc = ctx.getMeta(PC);
      final BoolSymbol jumpPc = pc.and(branchPredicateSymbol);
      final BoolSymbol noJumpPc = pc.and(branchPredicateSymbol.not());

      final boolean jumpFeasible = feasibilityChecker.satisfiable(jumpPc);
      final boolean noJumpFeasible = feasibilityChecker.satisfiable(noJumpPc);

      if(jumpFeasible && !noJumpFeasible) {
         jumpState = ctx;
         nojumpState = null;
      } else if(noJumpFeasible && !jumpFeasible) {
         jumpState = null;
         nojumpState = ctx;
      } else {
         final JState[] states = ctx.fork();
         jumpState = states[0];
         nojumpState = states[1];
      }

      if(jumpState != null) {
         jumpState.advanceTo(ctx.instructionJmpTarget());
         jumpState.setMeta(PC, jumpPc);
      }

      if(nojumpState != null) {
         nojumpState.advanceTo(ctx.instructionNext());
         nojumpState.setMeta(PC, noJumpPc);
      }

      if(jumpFeasible && noJumpFeasible) {
         ctx.fork(new JState[]{jumpState, nojumpState});
      }
   }

   @Override
   public String toString() {
      return branchStrategy.toString();
   }

   public static Vop geInstruction(final FeasibilityChecker feasibilityChecker) {
      return ibranchInstruction(new GeStrategy(), feasibilityChecker);
   }

   public static Vop gtInstruction(final FeasibilityChecker feasibilityChecker) {
      return ibranchInstruction(new GtStrategy(), feasibilityChecker);
   }

   public static Vop leInstruction(final FeasibilityChecker feasibilityChecker) {
      return ibranchInstruction(new LeStrategy(), feasibilityChecker);
   }

   public static Vop ltInstruction(final FeasibilityChecker feasibilityChecker) {
      return ibranchInstruction(new LtStrategy(), feasibilityChecker);
   }

   public static Vop neInstruction(final FeasibilityChecker feasibilityChecker) {
      return ibranchInstruction(new NeStrategy(), feasibilityChecker);
   }

   public static Vop eqInstruction(final FeasibilityChecker feasibilityChecker) {
      return ibranchInstruction(new EqStrategy(), feasibilityChecker);
   }

   private static Vop ibranchInstruction(
         final UnarySBranchOp unaryStrategy,
         final FeasibilityChecker feasibilityChecker) {
      return new SBranchInstruction(new UnarySBranchStrategy(unaryStrategy), feasibilityChecker);
   }

   public static Vop icmpgeInstruction(final FeasibilityChecker feasibilityChecker) {
      return icmpInstruction(new ICmpGeStrategy(), feasibilityChecker);
   }

   public static Vop icmpleInstruction(final FeasibilityChecker feasibilityChecker) {
      return icmpInstruction(new ICmpLeStrategy(), feasibilityChecker);
   }

   public static Vop icmpneInstruction(final FeasibilityChecker feasibilityChecker) {
      return icmpInstruction(new ICmpNeStrategy(), feasibilityChecker);
   }

   public static Vop icmpeqInstruction(final FeasibilityChecker feasibilityChecker) {
      return icmpInstruction(new ICmpEqStrategy(), feasibilityChecker);
   }

   public static Vop icmpgtInstruction(final FeasibilityChecker feasibilityChecker) {
      return icmpInstruction(new ICmpGtStrategy(), feasibilityChecker);
   }

   public static Vop icmpltInstruction(final FeasibilityChecker feasibilityChecker) {
      return icmpInstruction(new ICmpLtStrategy(), feasibilityChecker);
   }

   private static Vop icmpInstruction(
         final BinarySBranchOp icmpStrategy,
         final FeasibilityChecker feasibilityChecker) {
      return new SBranchInstruction(new BinarySBranchStrategy(icmpStrategy), feasibilityChecker);
   }

   @Override public <T> T query(final InstructionQuery<T> instructionQuery) {
      return instructionQuery.branch();
   }
}