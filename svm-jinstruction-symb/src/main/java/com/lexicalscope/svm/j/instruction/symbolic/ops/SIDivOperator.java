package com.lexicalscope.svm.j.instruction.symbolic.ops;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.DivSymbol;

public class SIDivOperator implements SIBinaryOperator {
    @Override
    public String toString() {
        return "S IDIV";
    }

    @Override public Integer eval(final Integer value1, final Integer value2) {
        return value1 / value2;
    }

    @Override public ISymbol eval(final ISymbol svalue1, final ISymbol svalue2) {
        return new DivSymbol(svalue1, svalue2);
    }
}
