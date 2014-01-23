package com.lexicalscope.symb.classloading;

import static com.lexicalscope.symb.klass.SClassMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.j.instruction.factory.Instructions;
import com.lexicalscope.symb.klass.SClass;

public class TestThreadIsLoadedFromLocal {
   @Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
   @Mock private Instructions instructions;
   @Mock private ClassLoaded classLoaded;

   @Test public void javaLangThreadLocalVersionIsLoaded() {
      context.checking(new Expectations(){{
         exactly(2).of(classLoaded).loaded(with(nameIs(Object.class)));
         oneOf(classLoaded).loaded(with(nameIs(Runnable.class)));
         oneOf(classLoaded).loaded(with(nameIs(Thread.class)));
      }});

      final SClass loaded = new AsmSClassLoader().load(Thread.class, classLoaded);
      assertThat(loaded, loadedFromSamePlaceAs(AsmSClassLoader.class));
   }
}