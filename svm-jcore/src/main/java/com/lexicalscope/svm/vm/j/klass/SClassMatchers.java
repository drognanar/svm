package com.lexicalscope.svm.vm.j.klass;

import static com.lexicalscope.svm.vm.j.KlassInternalName.internalName;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public class SClassMatchers {
   public static int withIndex(final int i) {
      return i;
   }

   public static Matcher<? super SClass> hasField(final String name, final int withIndex) {
      return hasField(null, name, withIndex);
   }

   public static Matcher<? super SClass> hasFieldAtIndex(final int index, final String withName) {
      return new TypeSafeDiagnosingMatcher<SClass>(SClass.class) {
         @Override
         public void describeTo(final Description description) {
            description.appendText("field called ").appendValue(withName).appendText(" at index ").appendValue(index);
         }

         @Override
         protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            mismatchDescription.appendText("field called ").appendValue(item.fieldAtIndex(index));
            return item.fieldAtIndex(index).name().getName().equals(withName);
         }
      };
   }

   public static Matcher<? super SClass> hasStaticField(final String name, final int withIndex) {
      return new TypeSafeDiagnosingMatcher<SClass>(SClass.class) {
         @Override
         public void describeTo(final Description description) {
            description.appendText("field called ").appendValue(name).appendText(" at index ").appendValue(withIndex);
         }

         @Override
         protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            final SFieldName qualifiedFieldName = new SFieldName(item.name(), name);

            mismatchDescription.appendValue(item);
            return item.hasStaticField(qualifiedFieldName) && item.staticFieldIndex(qualifiedFieldName) == withIndex;
         }
      };
   }

   public static Matcher<? super SClass> hasField(final SClass definingClass, final String name, final int withIndex) {
      return new TypeSafeDiagnosingMatcher<SClass>(SClass.class) {
         @Override
         public void describeTo(final Description description) {
            description.appendText("field called ").appendValue(name).appendText(" at index ").appendValue(withIndex);
         }

         @Override
         protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            final KlassInternalName definedIn = (definingClass != null ? definingClass : item).name();
            final SFieldName qualifiedFieldName = new SFieldName(definedIn, name);

            mismatchDescription.appendValue(item);
            return item.hasField(qualifiedFieldName) && item.fieldIndex(qualifiedFieldName) == withIndex;
         }
      };
   }

   public static Matcher<? super SClass> hasSuperclass(final Matcher<SClass> matcher) {
      return new TypeSafeDiagnosingMatcher<SClass>(SClass.class) {
         @Override
         public void describeTo(final Description description) {
            description.appendText("class with superclass matching ").appendDescriptionOf(matcher);
         }

         @Override
         protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            mismatchDescription.appendText("class with superclass ").appendValue(item.superclass());
            return matcher.matches(item.superclass());
         }};
   }

   public static Matcher<SClass> nameIs(final Class<?> klass) {
      return name(internalName(klass));
   }

   public static Matcher<SClass> name(final String internalName) {
      return name(internalName(internalName));
   }

   public static Matcher<SClass> name(final KlassInternalName internalName) {
      return new TypeSafeDiagnosingMatcher<SClass>(SClass.class) {
         @Override
         public void describeTo(final Description description) {
            description.appendText("class with name ").appendValue(internalName);
         }

         @Override
         protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            mismatchDescription.appendText("class with name ").appendValue(item.name());
            return equalTo(internalName).matches(item.name());
         }};
   }

   public static Matcher<? super SClass> isInstanceOf(final SClass klass) {
      return new TypeSafeDiagnosingMatcher<SClass>() {
         @Override public void describeTo(final Description description) {
            description.appendText("instanceOf ").appendValue(klass);
         }

         @Override protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            mismatchDescription.appendValue(item);
            return item.instanceOf(klass);
         }
      };
   }

   public static Matcher<? super SClass> loadedFromSamePlaceAs(final Class<?> klass) {
      final URL expectedLocation = klass.getProtectionDomain().getCodeSource().getLocation();

      return new TypeSafeDiagnosingMatcher<SClass>(){
         @Override public void describeTo(final Description description) {
            description.appendText("class loaded from ").appendValue(expectedLocation);
         }

         @Override protected boolean matchesSafely(final SClass item, final Description mismatchDescription) {
            mismatchDescription.appendText("class loaded from ").appendValue(item.loadedFrom());
            return item.loadedFrom().toExternalForm().startsWith(expectedLocation.toExternalForm());
         }
      };
   }
}
