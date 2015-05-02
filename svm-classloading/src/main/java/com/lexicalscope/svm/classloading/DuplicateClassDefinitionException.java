package com.lexicalscope.svm.classloading;

import com.lexicalscope.svm.vm.j.klass.SClass;


public class DuplicateClassDefinitionException extends RuntimeException {
   public DuplicateClassDefinitionException(final SClass klass) {
      super("" + klass.name());
   }
}
