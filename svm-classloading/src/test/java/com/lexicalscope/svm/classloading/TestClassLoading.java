package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestClassLoading {
   @Test public void loadAClass()  {
      assertNotNull(new AsmSClassLoader().load(internalName("com/lexicalscope/svm/classloading/EmptyClass")));
   }
}
