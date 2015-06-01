package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import com.lexicalscope.svm.heap.ObjectRef;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.natives.AbstractNativeMethodDef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

public class Symbolic_getArgument extends AbstractNativeMethodDef {
    public Symbolic_getArgument() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "getArgument",
                "([Ljava/lang/Object;)Ljava/lang/Object;");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        return statements(instructions).maxStack(2).maxLocals(1)
                .linearOp(new GetArgumentsOp())
                .return1(name()).build();
    }

    /**
     * An operation that invokes a method with specified parameters.
     */
    private class GetArgumentsOp implements Vop {
        @Override
        public void eval(JState ctx) {
            ObjectRef possibleValuesRef = (ObjectRef) ctx.pop();
            int possibleValueCount = (int) ctx.get(possibleValuesRef, 1);
            Object[] possibleValues = new Object[possibleValueCount];

            for (int i = 0; i < possibleValueCount; i++) {
                possibleValues[i] = ctx.get(possibleValuesRef, 2 + i);
            }

            Symbolic_selectState.pushValues(ctx, possibleValues);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
