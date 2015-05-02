package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.j.instruction.concrete.nativ3.DoubleToRawLongBits;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.MethodBody;

public class Java_lang_double_doubleToRawLongBits extends AbstractNativeMethodDef implements NativeMethodDef {
   public Java_lang_double_doubleToRawLongBits() {
      super(internalName("java/lang/Double"), "doubleToRawLongBits", "(D)J");
   }

   @Override public MethodBody instructions(final InstructionSource instructions) {
      return statements(instructions).maxStack(2).maxLocals(1).dload(0).linearOp(new DoubleToRawLongBits()).return2(name()).build();
   }
}
