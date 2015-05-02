package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.MethodBody;

public final class Java_lang_class_getClassLoader0 extends AbstractNativeMethodDef {
   public Java_lang_class_getClassLoader0() {
      super(internalName("java/lang/Class"), "getClassLoader0", "()Ljava/lang/ClassLoader;");
   }

   @Override public MethodBody instructions(final InstructionSource instructions) {
      return statements(instructions).maxStack(1).aconst_null().return1(name()).build();
   }
}
