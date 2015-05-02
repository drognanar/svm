package com.lexicalscope.svm.classloading;

import com.lexicalscope.svm.vm.j.KlassInternalName;

public class SClassNotFoundException extends RuntimeException {
   public SClassNotFoundException(final KlassInternalName name) {
      super("" + name);
   }
}
