package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.Instructions;
import com.lexicalscope.svm.j.statementBuilder.MethodBody;
import com.lexicalscope.symb.klass.SMethodDescriptor;

public interface NativeMethodDef {
   MethodBody instructions(Instructions instructions);
   SMethodDescriptor name();
}