package com.lexicalscope.svm.classloading;

import static com.lexicalscope.MatchersAdditional.has;
import static com.lexicalscope.svm.vm.j.JavaConstants.INIT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.lexicalscope.svm.vm.j.klass.SMethod;

public class TestMethodNamesCanBeObtainedFromDebugSymbols {
   private final SClassLoader sClassLoader = new AsmSClassLoader();

   public static class MethodNames {
      public MethodNames(final int a, final int b) { }

      public void aMethod(final int x, final int y) {}

      public static void staticMethod(final int s, final int t) { }
   }



   @Test public void canObtainMethodNamesOfVirtualMethod() {
      final SMethod virtualMethod = sClassLoader.virtualMethod(MethodNames.class, "aMethod", "(II)V");
      assertThat(virtualMethod.parameterNames(), has(equalTo("x"),equalTo("y")).only().inOrder());
   }

   @Test public void canObtainMethodNamesOfConstructor() {
      final SMethod virtualMethod = sClassLoader.virtualMethod(MethodNames.class, INIT, "(II)V");
      assertThat(virtualMethod.parameterNames(), has(equalTo("a"),equalTo("b")).only().inOrder());
   }

   @Test public void canObtainMethodNamesOfStaticMethod() {
      final SMethod virtualMethod = sClassLoader.virtualMethod(MethodNames.class, "staticMethod", "(II)V");
      assertThat(virtualMethod.parameterNames(), has(equalTo("s"),equalTo("t")).only().inOrder());
   }
}
