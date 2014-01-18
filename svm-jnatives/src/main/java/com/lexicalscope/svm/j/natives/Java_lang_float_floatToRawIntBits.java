package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.Instructions;
import com.lexicalscope.svm.j.statementBuilder.MethodBody;

public class Java_lang_float_floatToRawIntBits extends AbstractNativeMethodDef implements NativeMethodDef {
   public Java_lang_float_floatToRawIntBits() {
      super("java/lang/Float", "floatToRawIntBits", "(F)I");
   }

   @Override public MethodBody instructions(final Instructions instructions) {
      return instructions.statements().maxStack(1).maxLocals(1).fload(0).floatToRawIntBits().return1().build();
   }
}