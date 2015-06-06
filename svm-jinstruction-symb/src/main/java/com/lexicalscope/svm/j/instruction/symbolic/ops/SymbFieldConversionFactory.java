package com.lexicalscope.svm.j.instruction.symbolic.ops;

import com.lexicalscope.svm.j.instruction.concrete.object.FieldConversion;
import com.lexicalscope.svm.j.instruction.concrete.object.FieldConversionFactory;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;

public class SymbFieldConversionFactory implements FieldConversionFactory {
   private static final class IntToInt implements FieldConversion {
      @Override public Object convert(final Object val) {
         assert val instanceof Integer || val instanceof ISymbol;
         return val;
      }
   }

   private static final class NoConversion implements FieldConversion {
      @Override public Object convert(final Object val) {
         return val;
      }
   }

   private static final class IntToChar implements FieldConversion {
      @Override public Object convert(final Object val) {
         assert val instanceof Integer || val instanceof ISymbol;
         if(val instanceof ISymbol) {
            return val;
         }
         return (char)(int) val;
      }
   }

   private static final class CharToInt implements FieldConversion {
      @Override public Object convert(final Object val) {
         assert val instanceof Character || val instanceof ISymbol;
         if(val instanceof ISymbol) {
            return val;
         }
         return (int)(char) val;
      }
   }

   @Override
   public NoConversion noConversion() {
      return new NoConversion();
   }

   @Override
   public IntToInt intToInt() {
      return new IntToInt();
   }

   @Override
   public IntToChar intToChar() {
      return new IntToChar();
   }

   @Override public FieldConversion charToInt() {
      return new CharToInt();
   }
}
