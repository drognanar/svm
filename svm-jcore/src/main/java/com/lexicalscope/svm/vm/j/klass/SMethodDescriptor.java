package com.lexicalscope.svm.vm.j.klass;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.stack.trace.SMethodName;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.SVirtualMethodName;

public interface SMethodDescriptor extends SMethodName {
   boolean isVoidMethod();
   boolean isConstructor();

   KlassInternalName klassName();
   boolean declaredOn(KlassInternalName klassInternalName);
   boolean declaredOn(Matcher<KlassInternalName> klassInternalNameMatcher);

   String name();

   String desc();

   int[] objectArgIndexes();
   boolean returnIsObject();
   int argSize();
   int returnCount();

   SVirtualMethodName virtualName();
}