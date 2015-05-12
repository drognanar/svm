package com.lexicalscope.svm.vm.conc.junit;

import com.lexicalscope.svm.vm.j.StateTag;

public class ClassStateTag implements StateTag {
   private final Class<?> klass;

   public ClassStateTag(final Class<?> klass) {
      this.klass = klass;
   }

   @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (klass == null ? 0 : klass.hashCode());
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
      final ClassStateTag other = (ClassStateTag) obj;
      if (klass == null) {
         if (other.klass != null) {
            return false;
         }
      } else if (!klass.equals(other.klass)) {
         return false;
      }
      return true;
   }

   @Override public String toString() {
      return klass.getSimpleName();
   }

   public static StateTag tag(final Class<?> klass) {
      return new ClassStateTag(klass);
   }
}
