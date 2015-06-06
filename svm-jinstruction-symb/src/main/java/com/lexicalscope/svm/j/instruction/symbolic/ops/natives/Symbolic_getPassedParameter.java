package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.natives.AbstractNativeMethodDef;
import com.lexicalscope.svm.vm.j.*;

import java.util.Map;

import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.S_PARAMETERS;
import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.getObject;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;

public class Symbolic_getPassedParameter extends AbstractNativeMethodDef {
    public Symbolic_getPassedParameter() {
        super("com/lexicalscope/svm/j/instruction/symbolic/symbols/SymbolFactory", "getPassedParameter", "(Ljava/lang/Object;Ljava/lang/Class;I)Ljava/lang/Object;");
    }

    @Override
    public MethodBody instructions(InstructionSource instructions) {
        return statements(instructions)
                .maxStack(2)
                .maxLocals(1)
                .linearOp(new GetPassedParameterOp())
                .return1(name()).build();
    }

    private class GetPassedParameterOp implements Vop {
        @Override
        public void eval(JState ctx) {
            Object[] params = ctx.locals(3);
            Map passedParameters = ctx.getMeta(S_PARAMETERS);
            Object object = getObject(passedParameters, params[0], params[1]).get((int) params[2]);
            ctx.push(object);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
