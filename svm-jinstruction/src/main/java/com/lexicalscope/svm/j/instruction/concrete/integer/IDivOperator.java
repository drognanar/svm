package com.lexicalscope.svm.j.instruction.concrete.integer;

import com.lexicalscope.svm.j.instruction.concrete.ops.BinaryOperator;

public class IDivOperator implements BinaryOperator {
    @Override
    public Object eval(final Object left, final Object right) {
        return (int) left / (int) right;
    }

    @Override
    public String toString() {
        return "IDIV";
    }
}
