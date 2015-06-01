package com.lexicalscope.svm.j.instruction.symbolic.pc;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.FalseSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpEqSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpGeSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpGtSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpLeSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpLtSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IConstSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;

public class PcBuilder {
   public static ICmpEqSymbol icmpEq(final ISymbol value1, final ISymbol value2) {
      return new ICmpEqSymbol(value1, value2);
   }

   public static BoolSymbol icmplt(final ISymbol value1, final ISymbol value2) {
      return new ICmpLtSymbol(value1, value2);
   }

   public static BoolSymbol icmplt(final int i, final ISymbol symbol) {
      return icmplt(asISymbol(i), symbol);
   }

   public static BoolSymbol invert(final BoolSymbol value1) {
      return value1.not();
   }

   public static BoolSymbol icmpgt(final ISymbol value1, final ISymbol value2) {
      return new ICmpGtSymbol(value1, value2);
   }

   public static BoolSymbol icmpgt(final ISymbol symbol, final int i) {
      return icmpgt(symbol, asISymbol(i));
   }

   public static BoolSymbol icmpge(final ISymbol value1, final ISymbol value2) {
      return new ICmpGeSymbol(value1, value2);
   }

   public static BoolSymbol icmple(final ISymbol value1, final ISymbol value2) {
      return new ICmpLeSymbol(value1, value2);
   }

   public static ISymbol asISymbol(final Object symbolOrInt) {
      return symbolOrInt instanceof ISymbol ? (ISymbol) symbolOrInt : new IConstSymbol((int) symbolOrInt);
   }

   public static BoolSymbol and(final BoolSymbol left, final BoolSymbol right) {
      return left.and(right);
   }

   public static BoolSymbol or(final BoolSymbol left, final BoolSymbol right) {
      return left.or(right);
   }

   public static BoolSymbol truth() {
      return new TrueSymbol();
   }

   public static BoolSymbol falsity() {
      return new FalseSymbol();
   }
}
