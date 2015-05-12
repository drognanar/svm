package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.svm.partition.trace.HashTrace.CallReturn.CALL;

import java.util.Arrays;

import com.google.common.base.Joiner;
import com.lexicalscope.svm.partition.trace.HashTrace.CallReturn;
import com.lexicalscope.svm.stack.trace.SMethodName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class TraceElement {
   private final TraceElement previous;
   private final SMethodDescriptor method;
   private final CallReturn callReturn;
   private final Object[] args;

   TraceElement(
         final TraceElement previous,
         final SMethodDescriptor method,
         final CallReturn callReturn,
         final Object[] args) {
      assert !Arrays.asList(args).contains(null);
      this.previous = previous;
      this.method = method;
      this.callReturn = callReturn;
      this.args = args;
   }

   public TraceElement previous() {
      return previous;
   }

   public SMethodName methodName() {
      return method;
   }

   public boolean isCall() {
      return callReturn.equals(CALL);
   }

   public Object[] args() {
      return args;
   }

   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(args);
      result = prime * result + (callReturn == null ? 0 : callReturn.hashCode());
      result = prime * result + (method == null ? 0 : method.hashCode());
      result = prime * result + (previous == null ? 0 : previous.hashCode());
      return result;
   }

   @Override public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final TraceElement other = (TraceElement) obj;
      if (!Arrays.equals(args, other.args)) {
         return false;
      }
      if (callReturn != other.callReturn) {
         return false;
      }
      if (method == null) {
         if (other.method != null) {
            return false;
         }
      } else if (!method.equals(other.method)) {
         return false;
      }
      if (previous == null) {
         if (other.previous != null) {
            return false;
         }
      } else if (!previous.equals(other.previous)) {
         return false;
      }
      return true;
   }

   @Override public String toString() {
      return String.format("[%s]%s - (%s)", callReturn, method, Joiner.on(", ").join(args));
   }
}
