package com.lexicalscope.svm.j.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;

import java.util.Map;

import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.S_PARAMETERS;
import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.addMapping;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

/**
 * A method to which you pass a single parameter.
 */
public class Symbolic_passParameter extends AbstractNativeMethodDef {
    public Symbolic_passParameter() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "passParameter", "(Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/Object;)V");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        return statements(instructions)
                .maxStack(3)
                .maxLocals(1)
                .linearOp(new PassParameterOp())
                .returnVoid(name()).build();
    }

    private class PassParameterOp implements Vop {
        @Override
        public void eval(JState ctx) {
            Map passedParameters = ctx.getMeta(S_PARAMETERS);
            Object[] params = ctx.locals(3);
            Map newMap = addMapping(passedParameters, params[0], params[1], params[2]);
            ctx.setMeta(S_PARAMETERS, newMap);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
