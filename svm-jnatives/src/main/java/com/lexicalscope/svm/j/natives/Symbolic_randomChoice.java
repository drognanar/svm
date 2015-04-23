package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

public class Symbolic_randomChoice extends AbstractNativeMethodDef {
    public Symbolic_randomChoice() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "randomChoice", "()Z");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        return statements(instructions)
                .maxStack(1)
                .maxLocals(1)
                .linearOp(new RandomChoiceOp())
                .return1(name()).build();
    }

    private class RandomChoiceOp implements Vop {
        @Override
        public void eval(JState ctx) {
            JState[] forks = ctx.fork();
            forks[0].push(0);
            forks[1].push(1);
            ctx.fork(forks);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
