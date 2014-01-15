package com.lexicalscope.symb.vm.classloader;

import com.lexicalscope.symb.vm.SMethodName;

public interface MethodResolver {
   SMethod virtualMethod(SMethodDescriptor sMethodName);
   SMethod declaredMethod(SMethodName sMethodName);
   String name();
}