package com.lexicalscope.svm.heap;


public final class ObjectRef {
   private final int address;

   ObjectRef(final int address) {
      this.address = address;
   }

   int address() {
      return address;
   }

   @Override public int hashCode() {
      return address;
   }

   @Override public boolean equals(final Object obj) {
      if(obj == this) {
         return true;
      }
      if(obj != null && obj.getClass().equals(this.getClass())) {
         return ((ObjectRef) obj).address == address;
      }
      return false;
   }

   @Override
   public String toString() {
      return String.format("$%d", address);
   }
}
