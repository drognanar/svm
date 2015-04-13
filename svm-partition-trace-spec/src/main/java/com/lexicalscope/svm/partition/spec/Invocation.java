package com.lexicalscope.svm.partition.spec;

public interface Invocation {
   String methodName();
   Receiver receiver();
   Value parameter(String path);
}
