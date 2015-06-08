package com.lexicalscope.svm.j.instruction.concrete.exceptions;

import com.lexicalscope.svm.j.instruction.StateAssertion;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.Vop;

/**
 * Operation which throws an exception.
 * For now the exception cannot be caught.
 * It brings down a single program state which threw an exception.
 */
public class AThrowOp implements Vop {
    @Override
    public void eval(JState ctx) {
        StateAssertion.assertState(ctx, false, "Catching exceptions is not implemented.");
    }

    @Override
    public <T> T query(InstructionQuery<T> instructionQuery) {
        return instructionQuery.nativ3();
    }
}
