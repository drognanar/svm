package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

/**
 * Method which chooses a number randomly between 0 and (count - 1) creating n choices.
 */
public class Symbolic_selectState extends AbstractNativeMethodDef {
    public Symbolic_selectState() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "selectState", "(I)I");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        return statements(instructions)
                .maxStack(1)
                .maxLocals(1)
                .linearOp(new SelectStateOp())
                .return1(name()).build();
    }

    private class SelectStateOp implements Vop {
        @Override
        public void eval(JState ctx) {
            int count = (int) ctx.pop();
            JState[] forks = new JState[count];
            for (int i = 0; i < count; i++) {
                forks[i] = ctx.snapshot();
                forks[i].push(i);
            }
            ctx.fork(forks);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
