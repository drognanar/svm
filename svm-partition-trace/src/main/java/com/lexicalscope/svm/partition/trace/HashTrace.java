package com.lexicalscope.svm.partition.trace;

import static com.lexicalscope.svm.partition.trace.HashTrace.CallReturn.CALL;
import static java.lang.System.lineSeparator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class HashTrace implements Trace {
   private final Map<Object, Alias> map;
   private final int nextAlias;
   private final TraceElement head;

   public enum CallReturn { CALL, RETURN }

   public HashTrace() {
      this(null, new HashMap<Object, Alias>(), 0);
   }

   private HashTrace(
         final TraceElement trace,
         final Map<Object, Alias> map,
         final int nextAlias) {
      this.head = trace;
      this.nextAlias = nextAlias;
      this.map = map;
   }

   @Override
   public Trace extend(final SMethodDescriptor methodCalled, final CallReturn callReturn, final Object ... args) {
      final TraceExtender traceExtender = new TraceExtender(args, map, nextAlias);

      if(callReturn.equals(CALL)) {
         traceExtender.aliasesForCallArguments(methodCalled.objectArgIndexes());
      }
      if(callReturn.equals(CALL) || methodCalled.returnIsObject()) {
         traceExtender.aliasesForZerothArguments();
      }

      return new HashTrace(new TraceElement(head, methodCalled, callReturn, traceExtender.normalisedArgs()), traceExtender.newMap(), traceExtender.newNextAlias());
   }

   @Override
   public List<TraceElement> asList() {
      final LinkedList<TraceElement> result = new LinkedList<>();

      TraceElement element = head;
      while(element != null) {
         result.add(0, element);
         element = element.previous();
      }
      return result;
   }

   @Override public Iterator<TraceElement> iterator() {
      return asList().iterator();
   }

   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (head == null ? 0 : head.hashCode());
//      result = prime * result + (map == null ? 0 : map.hashCode());
      result = prime * result + nextAlias;
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
      final HashTrace other = (HashTrace) obj;
      if (head == null) {
         if (other.head != null) {
            return false;
         }
      } else if (!head.equals(other.head)) {
         return false;
      }
//      if (map == null) {
//         if (other.map != null) {
//            return false;
//         }
//      } else if (!map.equals(other.map)) {
//         return false;
//      }
      if (nextAlias != other.nextAlias) {
         return false;
      }
      return true;
   }

   @Override public String toString() {
      final StringBuilder traceToString = new StringBuilder();
      final List<TraceElement> trace = asList();
      traceToString.append(String.format("Trace of %s elements%n", trace.size()));
      for (final TraceElement element : trace) {
         traceToString.append("\t").append(element).append(lineSeparator());
      }
      return traceToString.toString();
   }
}
