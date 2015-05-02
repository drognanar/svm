package com.lexicalscope.svm.classloading;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestStatics {
   @Rule public ExpectedException exception = ExpectedException.none();

   private final SClassLoader classLoader = new AsmSClassLoader();
   private final StaticsImpl statics = new StaticsImpl(classLoader);

   @Test public void definedClassGivesClassRef() {
      statics.defineClass(internalName(EmptyClass.class));

      assertThat(classLoader.load(EmptyClass.class), sameInstance(statics.load(internalName(EmptyClass.class))));
   }

   @Test public void definedClassCannotBeRedefined() {
      statics.defineClass(internalName(EmptyClass.class));

      exception.expect(DuplicateClassDefinitionException.class);
      statics.defineClass(internalName(EmptyClass.class));
   }
}
