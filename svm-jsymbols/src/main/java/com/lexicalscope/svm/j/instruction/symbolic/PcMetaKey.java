package com.lexicalscope.svm.j.instruction.symbolic;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.metastate.MetaKey;

public final class PcMetaKey implements MetaKey<BoolSymbol> {
   public static final PcMetaKey PC = new PcMetaKey();

   private PcMetaKey() {}

   @Override public Class<BoolSymbol> valueType() {
      return BoolSymbol.class;
   }

   @Override public String toString() {
      return "PC";
   }
}
