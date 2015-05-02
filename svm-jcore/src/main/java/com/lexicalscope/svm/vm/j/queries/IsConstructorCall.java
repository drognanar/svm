package com.lexicalscope.svm.vm.j.queries;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.lexicalscope.svm.vm.j.InstructionQueryAdapter;
import com.lexicalscope.svm.vm.j.KlassInternalName;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

public class IsConstructorCall extends InstructionQueryAdapter<Boolean> {
   private final Matcher<KlassInternalName> klassInternalNameMatcher;

   public IsConstructorCall(final Class<?> klass) {
      this(internalName(klass));
   }

   public IsConstructorCall(final KlassInternalName klassInternalName) {
      this(Matchers.equalTo(klassInternalName));
   }

   public IsConstructorCall(final Matcher<KlassInternalName> klassInternalNameMatcher) {
      this.klassInternalNameMatcher = klassInternalNameMatcher;
   }

   @Override public Boolean invokespecial(final SMethodDescriptor methodName, final MethodArguments methodArguments) {
      return methodName.declaredOn(klassInternalNameMatcher) && methodName.isConstructor();
   }

   @Override protected Boolean defaultResult() {
      return false;
   }
}