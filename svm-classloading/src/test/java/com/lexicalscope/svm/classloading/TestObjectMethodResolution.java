package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.lexicalscope.svm.vm.j.JavaConstants;

public class TestObjectMethodResolution {
   private final SClassLoader classLoader = new AsmSClassLoader();

   @Test public void resolveMethodsFromObject()  {
      assertNotNull(
            classLoader
               .load(EmptyClass.class)
               .virtualMethod(JavaConstants.GET_CLASS));
   }

   @Test public void resolveOverriddenMethodFromObject()  {
      assertThat(
            classLoader
               .load(EmptyClass.class)
               .virtualMethod(JavaConstants.TO_STRING).name().klassName(),
            equalTo(internalName(Object.class)));

      assertThat(
            classLoader
               .load(OverriddenToString.class)
               .virtualMethod(JavaConstants.TO_STRING).name().klassName(),
            equalTo(internalName(OverriddenToString.class)));
   }
}
