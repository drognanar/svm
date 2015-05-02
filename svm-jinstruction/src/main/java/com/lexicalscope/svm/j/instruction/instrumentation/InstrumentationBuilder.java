package com.lexicalscope.svm.j.instruction.instrumentation;

import org.hamcrest.Matcher;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class InstrumentationBuilder {
   private final ListMultimap<Matcher<? super SMethodDescriptor>, MethodInstrumentor> instrumentationMap = LinkedListMultimap.create();

   public void instrument(final Matcher<? super SMethodDescriptor> methodMatcher, final MethodInstrumentor instrumentation) {
      assert methodMatcher != null;
      assert instrumentation != null;
      instrumentationMap.put(methodMatcher, instrumentation);
   }

   public Instrumentation instrumentation(final InstructionSource instructions) {
      if(instrumentationMap.isEmpty()) {
         return new NullInstrumentation();
      }
      return new MultimapInstrumentationContext(instructions, instrumentationMap);
   }
}
