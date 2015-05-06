package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

import java.util.Map;

import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.S_PARAMETERS;
import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.countObjects;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

/**
 * A method to which you pass a single parameter.
 */
public class Symbolic_countParameters extends AbstractNativeMethodDef {
    public Symbolic_countParameters() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "countParameters", "(Ljava/lang/Object;Ljava/lang/Class;)I");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        return statements(instructions)
                .maxStack(3)
                .maxLocals(1)
                .linearOp(new PassParameterOp())
                .return1(name()).build();
    }

    private class PassParameterOp implements Vop {
        @Override
        public void eval(JState ctx) {
            Object[] params = ctx.locals(2);
            Map passedParameters = ctx.getMeta(S_PARAMETERS);
            ctx.push(countObjects(passedParameters, params[0], params[1]));
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
