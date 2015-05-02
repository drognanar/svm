package com.lexicalscope.svm.classloading;

import static com.lexicalscope.MatchersAdditional.has;
import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static com.lexicalscope.svm.vm.j.klass.SClassMatchers.nameIs;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.lexicalscope.svm.vm.j.klass.SClass;

public class TestStaticsClassloadingOrder {
   @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

   @Mock private SClassLoader classLoader;
   @Mock private SClass subclass;
   @Mock private SClass superclass;
   @Mock private SClass anInterface;

   private StaticsImpl statics;


   @Before public void loadClass() {
      statics = new StaticsImpl(classLoader);
   }

   @Test public void classLoadingOrder() {
      context.checking(new Expectations(){{
         oneOf(classLoader).load(internalName(Subclass.class)); will(returnValue(subclass));
         atLeast(1).of(subclass).name(); will(returnValue(internalName(Subclass.class)));
         oneOf(subclass).superTypes(); will(returnValue(asList(subclass, superclass, anInterface)));
         atLeast(1).of(superclass).name(); will(returnValue(internalName(EmptyClass.class)));
         atLeast(1).of(anInterface).name(); will(returnValue(internalName(AnInterface.class)));
      }});

      final List<SClass> result = statics.defineClass(internalName(Subclass.class));

      assertThat(result, has(
            nameIs(EmptyClass.class),
            nameIs(Subclass.class),
            nameIs(AnInterface.class)).only().inAnyOrder());
   }
}
