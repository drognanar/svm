package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.concrete.LoadConstantArg;
import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.vm.j.MethodBody;

import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

public class Symbolic_selectState extends AbstractNativeMethodDef {
    public Symbolic_selectState() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "selectState", "(I)I");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        ITerminalSymbol symbol = new ITerminalSymbol("s1"); // needs to be a proper symbol.
        return statements(instructions)
                .maxStack(1)
                .maxLocals(1)
                .linearOp(new LoadConstantArg(symbol))
                .return1(name()).build();
    }
}
