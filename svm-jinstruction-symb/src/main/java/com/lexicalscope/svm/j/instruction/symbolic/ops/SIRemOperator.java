package com.lexicalscope.svm.j.instruction.symbolic.ops;

import com.lexicalscope.svm.j.instruction.symbolic.symbols.IRemSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;

public class SIRemOperator implements SIBinaryOperator {
    @Override
    public Integer eval(Integer value1, Integer value2) {
        return value1 % value2;
    }

    @Override
    public ISymbol eval(ISymbol svalue1, ISymbol svalue2) {
        return new IRemSymbol(svalue1, svalue2);
    }

    @Override
    public String toString() {
        return "S IREM";
    }
}
