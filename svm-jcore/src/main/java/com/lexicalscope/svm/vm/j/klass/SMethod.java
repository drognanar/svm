package com.lexicalscope.svm.vm.j.klass;

import java.util.List;

import com.lexicalscope.svm.vm.j.Instruction;


public interface SMethod {
   int maxLocals();

   int maxStack();

   Instruction entry();

   int argSize();

   SMethodDescriptor name();

   List<String> parameterNames();
}