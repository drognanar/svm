package com.lexicalscope.symb.vm.conc.junit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithEntryPoint {
   String value();
}
