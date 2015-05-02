package com.lexicalscope.svm.vm.j.klass;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public class SFieldName implements Comparable<SFieldName> {
   private final KlassInternalName definedIn;
   private final String name;

   public SFieldName(final KlassInternalName definedIn, final String name) {
      this.definedIn = definedIn;
      this.name = name;
   }

   @Override
   public int compareTo(final SFieldName o) {
      final int firstCompare = this.definedIn.compareTo(o.definedIn);
      if(firstCompare == 0) {
         return this.name.compareTo(o.name);
      }
      return firstCompare;
   }

   @Override
   public boolean equals(final Object obj) {
      if (obj == this) {
         return true;
      }

      if(obj != null && obj.getClass().equals(this.getClass())) {
         final SFieldName that = (SFieldName) obj;
         return that.definedIn.equals(definedIn) && that.name.equals(name);
      }
      return false;
   }

   public String getName() {
      return name;
   }

   @Override
   public int hashCode() {
      return definedIn.hashCode() ^ name.hashCode();
   }

   @Override
   public String toString() {
      return definedIn + "." + name;
   }
}
