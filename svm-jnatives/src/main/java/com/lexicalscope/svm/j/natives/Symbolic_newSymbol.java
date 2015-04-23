package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.concrete.LoadConstantArg;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.vm.j.MethodBody;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

/**
 * Class that allows dynamic creation of int symbols in execution.
 */
public class Symbolic_newSymbol extends AbstractNativeMethodDef {
    private static int counter = 0;

    public Symbolic_newSymbol(String methodName, String signature) {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", methodName, signature);
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        ITerminalSymbol symbol = getUniqueSymbol();
        return statements(instructions)
                .maxStack(1)
                .maxLocals(1)
                .linearOp(new LoadConstantArg(symbol))
                .return1(name()).build();
    }

    /**
     * Returns a symbol with a unique name.
     * @return A fresh symbol with a unique name.
     */
    public ITerminalSymbol getUniqueSymbol() {
        return getUniqueSymbol("symbol");
    }

    /**
     * Returns a symbol with a unique name.
     * @return A fresh symbol with a unique name.
     */
    public ITerminalSymbol getUniqueSymbol(String prefix) {
        ITerminalSymbol symbol = new ITerminalSymbol(String.format("%s%d", prefix, counter));
        counter++;
        return symbol;
    }
}
