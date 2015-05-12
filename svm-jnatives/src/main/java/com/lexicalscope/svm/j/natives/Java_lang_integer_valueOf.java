package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.vm.j.JavaConstants.*;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;

public class Java_lang_integer_valueOf extends AbstractNativeMethodDef {
   public Java_lang_integer_valueOf() {
      super("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
   }

   @Override public MethodBody instructions(final InstructionSource instructions) {
      return statements(instructions).
            maxStack(3).
            maxLocals(1).
            newObject(INTEGER_CLASS).
            dup().
            aload(0).
            invokeSpecial(new AsmSMethodName(INTEGER_CLASS, INIT, "(I)V")).
            return1(name()).build();
   }
}
