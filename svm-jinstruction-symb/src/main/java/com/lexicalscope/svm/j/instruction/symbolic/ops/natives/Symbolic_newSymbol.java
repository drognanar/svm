package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolCounterMetaKey.SC;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ISymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.j.natives.AbstractNativeMethodDef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

import java.util.ArrayList;

/**
 * Class that allows dynamic creation of int symbols in execution.
 */
public class Symbolic_newSymbol extends AbstractNativeMethodDef {
    public static final int INITIAL_CAPACITY = 128;
    private static ArrayList<ISymbol> symbols = new ArrayList<>(INITIAL_CAPACITY);
    {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            symbols.add(i, new ITerminalSymbol(String.format("s%d", i)));
        }
    }

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

    public static ISymbol getNewSymbol(JState ctx) {
        int counter = ctx.getMeta(SC);
        ISymbol symbol;
        if (counter < symbols.size()) {
            symbol = symbols.get(counter);
        } else {
            symbol = new ITerminalSymbol(counter);
            symbols.add(symbol);
        }
        ctx.setMeta(SC, counter + 1);
        return symbol;
    }
}
