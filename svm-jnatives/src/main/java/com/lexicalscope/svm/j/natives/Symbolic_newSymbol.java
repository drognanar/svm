package com.lexicalscope.svm.j.natives;

import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolCounterMetaKey.SC;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

/**
 * Class that allows dynamic creation of int symbols in execution.
 */
public class Symbolic_newSymbol extends AbstractNativeMethodDef {
    public Symbolic_newSymbol(final String methodName, final String signature) {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", methodName, signature);
    }

    @Override
    public MethodBody instructions(final InstructionSource instructions) {
        return statements(instructions)
                .maxStack(1)
                .maxLocals(1)
                .linearOp(new GetSymbolOp())
                .return1(name()).build();
    }

    private class GetSymbolOp implements Vop {
        @Override
        public void eval(JState ctx) {
            ctx.push(getNewSymbol(ctx));
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }

    public static ITerminalSymbol getNewSymbol(JState ctx) {
        int counter = ctx.getMeta(SC);
        ITerminalSymbol symbol = new ITerminalSymbol(String.format("%s%d", "symbol", counter));
        ctx.setMeta(SC, counter + 1);
        return symbol;
    }
}
