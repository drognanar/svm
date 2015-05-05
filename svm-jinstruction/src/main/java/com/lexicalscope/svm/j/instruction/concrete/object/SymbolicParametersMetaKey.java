package com.lexicalscope.svm.j.instruction.concrete.object;

import com.lexicalscope.svm.metastate.MetaKey;

import java.util.Map;

/**
 * Contains the mapping of parameters passed to abstract objects.
 */
public class SymbolicParametersMetaKey implements MetaKey<Map> {
    public static final SymbolicParametersMetaKey S_PARAMETERS = new SymbolicParametersMetaKey();

    private SymbolicParametersMetaKey() {}

    @Override public Class<Map> valueType() {
        return Map.class;
    }

    @Override public String toString() {
        return "SymbolicParameters";
    }
}
