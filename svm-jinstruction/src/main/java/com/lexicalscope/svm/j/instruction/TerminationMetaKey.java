package com.lexicalscope.svm.j.instruction;

import com.lexicalscope.svm.metastate.MetaKey;

/**
 * Meta key which determines if the vm has terminated.
 */
public class TerminationMetaKey implements MetaKey<Boolean> {
    public static final MetaKey<Boolean> TERMINATION = new TerminationMetaKey();
    private TerminationMetaKey() { }

    @Override public Class<Boolean> valueType() {
        return Boolean.class;
    }

    @Override public String toString() {
      return "Termination";
   }
}
