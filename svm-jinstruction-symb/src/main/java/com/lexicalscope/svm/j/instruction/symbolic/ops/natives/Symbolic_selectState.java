package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import com.lexicalscope.svm.j.instruction.factory.InstructionSource;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.BoolSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ICmpEqSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.IConstSymbol;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.ITerminalSymbol;
import com.lexicalscope.svm.j.natives.AbstractNativeMethodDef;
import com.lexicalscope.svm.j.natives.Symbolic_newSymbol;
import com.lexicalscope.svm.partition.trace.HashTrace;
import com.lexicalscope.svm.partition.trace.Trace;
import com.lexicalscope.svm.vm.j.InstructionQuery;
import com.lexicalscope.svm.vm.j.JState;
import com.lexicalscope.svm.vm.j.MethodBody;
import com.lexicalscope.svm.vm.j.Vop;
import com.lexicalscope.svm.vm.j.klass.SMethodDescriptor;

import static com.lexicalscope.svm.j.instruction.symbolic.PcMetaKey.PC;
import static com.lexicalscope.svm.j.statementBuilder.StatementBuilder.statements;
import static com.lexicalscope.svm.partition.trace.TraceMetaKey.TRACE;

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

    public static void pushValues(JState ctx, Object[] values) {
        SMethodDescriptor context = (SMethodDescriptor) ctx.currentFrame().context();
        Object callee = ctx.currentFrame().local(0);
        Trace trace = ctx.getMeta(TRACE);
        trace = trace.extend(context, HashTrace.CallReturn.CALL, callee);
        ctx.setMeta(TRACE, trace);

        JState[] forks = new JState[values.length];
        ITerminalSymbol selectionSymbol = Symbolic_newSymbol.getNewSymbol("select", ctx);
        for (int i = 0; i < values.length; i++) {
            forks[i] = ctx.snapshot();
            forks[i].push(values[i]);
            BoolSymbol previousConstraints = forks[i].getMeta(PC);
            BoolSymbol newConstraints = previousConstraints.and(new ICmpEqSymbol(selectionSymbol, new IConstSymbol(i)));
            forks[i].setMeta(PC, newConstraints);

            Trace forkTrace = trace.extend(context, HashTrace.CallReturn.RETURN, callee, i);
            forks[i].setMeta(TRACE, forkTrace);
        }

        ctx.forkDisjoined(forks);
    }

    private class SelectStateOp implements Vop {
        @Override
        public void eval(JState ctx) {
            int count = (int) ctx.pop();
            assert count > 0: "Cannot select one from 0 states.";
            Object[] values = new Object[count];
            for (int i = 0; i < count; i++) {
                values[i] = i;
            }

            pushValues(ctx, values);
        }

        @Override
        public <T> T query(InstructionQuery<T> instructionQuery) {
            return instructionQuery.nativ3();
        }
    }
}
