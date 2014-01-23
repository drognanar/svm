package com.lexicalscope.symb.classloading;

import static com.lexicalscope.symb.vm.j.j.klass.SClassMatchers.nameIs;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.symb.classloading.AsmSClassLoader;
import com.lexicalscope.symb.classloading.ClassLoaded;
import com.lexicalscope.symb.classloading.SClassLoader;

public class TestClassLoaderLoadOrder {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
   private final SClassLoader sClassLoader = new AsmSClassLoader();

   @Mock private ClassLoaded classLoaded;
   @Auto private Sequence loadSequence;

   @Test public void loadWillLoadSuperClassFirst(){
      context.checking(new Expectations(){{
         oneOf(classLoaded).loaded(with(nameIs(Object.class))); inSequence(loadSequence);
         oneOf(classLoaded).loaded(with(nameIs(ClassWith5Fields.class))); inSequence(loadSequence);
         oneOf(classLoaded).loaded(with(nameIs(SubClassWithAdditionalFields.class))); inSequence(loadSequence);
      }});

      assertThat(sClassLoader.load(SubClassWithAdditionalFields.class, classLoaded), nameIs(SubClassWithAdditionalFields.class));
   }
}
