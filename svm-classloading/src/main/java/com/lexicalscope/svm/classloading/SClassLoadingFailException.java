package com.lexicalscope.svm.classloading;

import com.lexicalscope.svm.vm.j.KlassInternalName;


public class SClassLoadingFailException extends RuntimeException {
   public SClassLoadingFailException(final KlassInternalName message, final Exception cause) {
      this("" + message, cause);
   }

   public SClassLoadingFailException(final String message, final Exception cause) {
      super(message, cause);
   }
}
