package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.svm.partition.trace.HashTrace.CallReturn.*;

import com.lexicalscope.svm.vm.j.JavaConstants;
import com.lexicalscope.svm.vm.j.code.AsmSMethodName;

public class TraceBuilder {
   private Trace trace = new HashTrace();

   public static TraceBuilder trace() { return new TraceBuilder(); }

   public TraceBuilder methodCall(final Class<?> klass, final String name, final String desc, final Object ... args) {
      trace = trace.extend(null, new AsmSMethodName(klass, name, desc), CALL, args);
      return this;
   }

   public TraceBuilder methodReturn(final Class<?> klass, final String name, final String desc, final Object ... args) {
      trace = trace.extend(null, new AsmSMethodName(klass, name, desc), RETURN, args);
      return this;
   }

   public TraceBuilder terminate() {
      trace = terminateTrace(trace);
      return this;
   }

   public static Trace terminateTrace(final Trace trace) {
      return trace.extend(null, JavaConstants.INITIAL_FRAME_NAME, RETURN);
   }

   public Trace build() {
      return trace;
   }

   @Override public String toString() {
      return trace.toString();
   }
}
