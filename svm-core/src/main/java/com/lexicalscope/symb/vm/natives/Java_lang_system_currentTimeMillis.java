package com.lexicalscope.symb.vm.natives;

import com.lexicalscope.symb.vm.classloader.MethodBody;
import com.lexicalscope.symb.vm.instructions.Instructions;

public class Java_lang_system_currentTimeMillis extends AbstractNativeMethodDef implements NativeMethodDef {
   public Java_lang_system_currentTimeMillis() {
      super("java/lang/System", "currentTimeMillis", "()J");
   }

   @Override public MethodBody instructions(final Instructions instructions) {
      return instructions.statements().maxStack(2).currentTimeMillis().return2().build();
   }
}