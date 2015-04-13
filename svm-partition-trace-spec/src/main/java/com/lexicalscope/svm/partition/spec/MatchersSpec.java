package com.lexicalscope.svm.partition.spec;

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class MatchersSpec {
   public static Matcher<Receiver> objectClass(final Matcher<String> klass) {
      return new FeatureMatcher<Receiver, String>(klass, "class", "class") {
         @Override protected String featureValueOf(final Receiver actual) {
            return actual.klass();
         }
      };
   }

   public static Matcher<? super CallContext> receiverClass(final String klass) {
      return receiver(objectClass(equalTo(klass)));
   }

   public static Matcher<Receiver> klassIn(String... klasses) {
       return klassIn(new HashSet<>(Arrays.asList(klasses)));
   }

   public static Matcher<Receiver> klassIn(final Set<String> klasses) {
      return objectClass(isIn(klasses));
   }

   public static Matcher<Invocation> receiver(final Matcher<Receiver> matcher) {
      return new FeatureMatcher<Invocation, Receiver>(matcher, "receiver", "receiver") {
         @Override protected Receiver featureValueOf(final Invocation actual) {
            return actual.receiver();
         }
      };
   }

   public static Matcher<Receiver> field(final String path, final Matcher<? super Field> matcher) {
      return new FeatureMatcher<Receiver, Field>(matcher, "receiverField " + path, "receiverField") {
         @Override protected Field featureValueOf(final Receiver actual) {
            return actual.field(path);
         }
      };
   }

    public static Matcher<CallContext> calledBy(final Matcher<String> matcher) {
        return new FeatureMatcher<CallContext, String>(matcher, "receiver", "receiver") {
            @Override protected String featureValueOf(CallContext actual) {
                return actual.methodName();
            }
        };
    }

   public static Matcher<Invocation> parameter(final String path, final Matcher<Value> matcher) {
      return new FeatureMatcher<Invocation, Value>(matcher, "parameter " + path, "parameter") {
         @Override protected Value featureValueOf(final Invocation actual) {
            return actual.parameter(path);
         }
      };
   }

   public static Matcher<Value> value(final Matcher<Object> matcher) {
      return new FeatureMatcher<Value, Object>(matcher, "value", "value") {
         @Override protected Object featureValueOf(final Value actual) {
            return actual.value();
         }
      };
   }

   public static Matcher<Object> integer(final Matcher<Integer> matcher) {
      return new TypeSafeDiagnosingMatcher<Object>() {
         @Override public void describeTo(final Description description) {
            description.appendText("instanceof integer and ").appendDescriptionOf(matcher);
         }

         @Override protected boolean matchesSafely(final Object item, final Description mismatchDescription) {
            if (!(item instanceof Integer)) {
               mismatchDescription.appendText("instanceof integer but ");
               matcher.describeMismatch(item, mismatchDescription);
               return false;
            }
            return matcher.matches(item);
         }
      };
   }

   public static Matcher<CallContext> previously(
         final String klass,
         final String method,
         final Matcher<Invocation> matcher) {
      return new FeatureMatcher<CallContext, Invocation>(matcher, "previously " + klass + "." + method, "previously") {
         @Override protected Invocation featureValueOf(final CallContext actual) {
            return actual.previously(klass, method);
         }
      };
   }
}
