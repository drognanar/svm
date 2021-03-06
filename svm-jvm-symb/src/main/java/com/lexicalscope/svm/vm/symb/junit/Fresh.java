package com.lexicalscope.svm.vm.symb.junit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Fresh {
   static class Default {}

   Class<?> type() default Default.class;
}
