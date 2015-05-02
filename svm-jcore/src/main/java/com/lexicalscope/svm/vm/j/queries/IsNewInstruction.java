package com.lexicalscope.svm.vm.j.queries;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;

import com.lexicalscope.svm.vm.j.InstructionQueryAdapter;
import com.lexicalscope.svm.vm.j.KlassInternalName;

public class IsNewInstruction extends InstructionQueryAdapter<Boolean> {
   private final Matcher<KlassInternalName> klassInternalNameMatcher;

   public IsNewInstruction(final Class<?> klass) {
      this(internalName(klass));
   }

   public IsNewInstruction(final KlassInternalName klassInternalName) {
      this(equalTo(klassInternalName));
   }

   public IsNewInstruction(final String klassInternalName) {
      this(internalName(klassInternalName));
   }

   public IsNewInstruction(final Matcher<KlassInternalName> klassInternalNameMatcher) {
      this.klassInternalNameMatcher = klassInternalNameMatcher;
   }

   @Override public Boolean newobject(final KlassInternalName klassDesc) {
      return klassInternalNameMatcher.matches(klassDesc);
   }

   @Override protected Boolean defaultResult() {
      return false;
   }
}