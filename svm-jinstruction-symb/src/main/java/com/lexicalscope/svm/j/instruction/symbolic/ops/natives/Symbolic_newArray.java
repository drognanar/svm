package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolCounterMetaKey.SC;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.LoadSymbolicArrayArg;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayAndLengthSymbols;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IArrayTerminalSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.j.natives.AbstractNativeMethodDef;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

/**
 * Class that allows dynamic creation of int symbols in execution.
 */
public class Symbolic_newArray extends AbstractNativeMethodDef {
    public Symbolic_newArray(final String methodName, final String signature) {
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
            new LoadSymbolicArrayArg(getNewSymbol("symbol", ctx)).eval(ctx);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }

    public static IArrayAndLengthSymbols getNewSymbol(String prefix, JState ctx) {
        int counter = ctx.getMeta(SC);
        String methodName = ctx.previousFrame().context().toString();
        String symbolName = String.format("%s_%s_%d", prefix, methodName, counter);
        ITerminalSymbol symbol = new ITerminalSymbol(symbolName);
        IArrayTerminalSymbol arraySymbol = new IArrayTerminalSymbol(counter);
        IArrayAndLengthSymbols symbolicArray = new IArrayAndLengthSymbols(arraySymbol, symbol);

        ctx.setMeta(SC, counter + 1);
        return symbolicArray;
    }
}
