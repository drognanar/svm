package com.lexicalscope.svm.partition.spec;

public interface Invocation {
   String callerMethodName();
   Receiver receiver();
   Value parameter(String path);
   Value callerParameter(int index);
   Value calleeParameter(int index);
}
