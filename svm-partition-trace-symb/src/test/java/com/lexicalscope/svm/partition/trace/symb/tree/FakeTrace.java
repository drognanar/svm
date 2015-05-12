package com.lexicalscope.svm.partition.trace.symb.tree;

import java.util.Iterator;
import java.util.List;

import com.lexicalscope.svm.partition.trace.HashTrace.CallReturn;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.partition.trace.TraceElement;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class FakeTrace implements Trace {
   private final String string;

   public FakeTrace(final String string) {
      this.string = string;
   }

   public FakeTrace() {
      string = super.toString();
   }

   @Override public String toString() {
      return string;
   }

   @Override public Iterator<TraceElement> iterator() {
      return null;
   }

   @Override public Trace extend(final SMethodDescriptor methodCalled, final CallReturn callReturn, final Object... args) {
      return null;
   }

   @Override public List<TraceElement> asList() {
      return null;
   }
}
