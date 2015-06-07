package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.MethodBody;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

public class Java_lang_throwable_fillInStackTrace extends AbstractNativeMethodDef implements NativeMethodDef {
    public Java_lang_throwable_fillInStackTrace() {
        super("java/lang/Throwable", "fillInStackTrace", "(I)Ljava/lang/Throwable;");
    }

    @Override public MethodBody instructions(final InstructionSource instructions) {
        return statements(instructions).maxStack(1).maxLocals(1)
                .aload(0)
                .return1(name()).build();
    }
}
