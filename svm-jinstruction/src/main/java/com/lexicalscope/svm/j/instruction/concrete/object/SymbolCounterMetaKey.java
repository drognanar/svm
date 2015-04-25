package com.lexicalscope.svm.j.instruction.concrete.object;

import com.lexicalscope.svm.metastate.MetaKey;

public class SymbolCounterMetaKey implements MetaKey<Integer> {
    public static final SymbolCounterMetaKey SC = new SymbolCounterMetaKey();

    private SymbolCounterMetaKey() {}

    @Override public Class<Integer> valueType() {
        return Integer.class;
    }

    @Override public String toString() {
        return "SymbolCounter";
    }
}
