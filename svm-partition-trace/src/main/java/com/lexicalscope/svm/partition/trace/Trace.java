package com.lexicalscope.svm.partition.trace;

import java.util.List;

import com.lexicalscope.svm.partition.trace.HashTrace.CallReturn;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public interface Trace extends Iterable<TraceElement> {
   Trace extend(SMethodDescriptor methodCalled, CallReturn callReturn, Object... args);

   List<TraceElement> asList();
}