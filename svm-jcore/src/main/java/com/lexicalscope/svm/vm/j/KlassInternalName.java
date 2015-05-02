package com.lexicalscope.svm.vm.j;

import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.objectweb.asm.Type;

public class KlassInternalName implements Comparable<KlassInternalName> {
   private final String internalName;

   public KlassInternalName(final String internalName) {
      assert !internalName.contains(".");
      this.internalName = internalName;
   }

   public static KlassInternalName internalName(final String internalName) {
      return new KlassInternalName(internalName);
   }

   public static KlassInternalName internalName(final Class<?> klass) {
      return internalName(Type.getInternalName(klass));
   }

   public static List<KlassInternalName> internalName(final List<String> names) {
      final List<KlassInternalName> result = new ArrayList<KlassInternalName>();
      for (final String name : names) {
         result.add(internalName(name));
      }
      return result;
   }

   public String componentType() {
      return isArrayClass() ? arrayContentClassName() : null;
   }

   private String arrayContentClassName() {
      return toClassName(string().substring(1));
   }

   public boolean isArrayClass() {
      return string().startsWith("[");
   }

   private String toClassName(final String substring) {
      switch (substring) {
         case "Z":
            return "boolean";
         case "C":
            return "char";
         case "B":
            return "byte";
         case "S":
            return "short";
         case "I":
            return "int";
         case "J":
            return "long";
         case "F":
            return "float";
         case "D":
            return "double";
         case "Ljava/lang/Object;":
            return "java/lang/Object";
      }
      return substring;
   }

   public String string() {
      return internalName;
   }

   @Override public int compareTo(final KlassInternalName o) {
      return this.internalName.compareTo(o.internalName);
   }

   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (internalName == null ? 0 : internalName.hashCode());
      return result;
   }

   @Override public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final KlassInternalName other = (KlassInternalName) obj;
      if (internalName == null) {
         if (other.internalName != null) {
            return false;
         }
      } else if (!internalName.equals(other.internalName)) {
         return false;
      }
      return true;
   }

   @Override public String toString() {
      return string();
   }

   public static Matcher<KlassInternalName> anyKlass() {
      return Matchers.any(KlassInternalName.class);
   }

   public static Matcher<KlassInternalName> matchingKlass(final Class<?> klass) {
      return equalTo(internalName(klass));
   }
}
