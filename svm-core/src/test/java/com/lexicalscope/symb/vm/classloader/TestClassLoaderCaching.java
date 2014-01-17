package com.lexicalscope.symb.vm.classloader;

import static com.lexicalscope.symb.vm.classloader.SClassMatchers.hasSuperclass;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.Test;

import com.lexicalscope.symb.vm.SClass;

public class TestClassLoaderCaching {
   private final SClassLoader sClassLoader = new AsmSClassLoader();
   private final SClass classWithFiveFields = sClassLoader.load(ClassWith5Fields.class);

   @Test public void loadSameObjectTwiceReturnsCachedCopy(){
      assertThat(sClassLoader.load(ClassWith5Fields.class), sameInstance(classWithFiveFields));
   }

   @Test public void superclassIsCachedCopy(){
      sClassLoader.load(SubClassWithAdditionalFields.class);
      assertThat(sClassLoader.load(SubClassWithAdditionalFields.class), hasSuperclass(sameInstance(classWithFiveFields)));
   }
}
