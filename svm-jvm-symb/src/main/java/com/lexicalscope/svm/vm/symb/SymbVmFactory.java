package com.lexicalscope.svm.vm.symb;

import static com.lexicalscope.svm.j.instruction.TerminationMetaKey.*;
import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey.S_PARAMETERS;
import static com.lexicalscope.svm.j.instruction.concrete.object.SymbolCounterMetaKey.SC;
import static com.lexicalscope.svm.j.instruction.symbolic.PcMetaKey.PC;
import static com.lexicalscope.svm.vm.conc.InitialStateBuilder.initialState;

import com.lexicalscope.svm.heap.HeapFactory;
import com.lexicalscope.svm.j.instruction.TerminationMetaKey;
import com.lexicalscope.svm.j.instruction.concrete.object.SymbolicParametersMetaKey;
import com.lexicalscope.svm.j.instruction.symbolic.SymbInstructionFactory;
import com.lexicalscope.svm.j.instruction.symbolic.ops.natives.SymbolicNativeMethods;
import com.lexicalscope.svm.j.instruction.symbolic.symbols.TrueSymbol;
import com.lexicalscope.svm.vm.conc.DepthFirstStateSearchFactory;
import com.lexicalscope.svm.vm.conc.FastHeapFactory;
import com.lexicalscope.svm.vm.conc.JvmBuilder;
import com.lexicalscope.svm.vm.conc.StateSearchFactory;

import java.util.HashMap;

public class SymbVmFactory {
    public static JvmBuilder symbolicVmBuilder(
            final SymbInstructionFactory instructionFactory,
            final StateSearchFactory searchFactory) {
        final HeapFactory heapFactory;
        if(JvmBuilder.class.desiredAssertionStatus()) {
            heapFactory = new CheckingSymbolicHeapFactory();
        } else {
            heapFactory = new FastHeapFactory();
        }

        final JvmBuilder vmBuilder = new JvmBuilder()
                .searchWith(searchFactory)
                .initialState(initialState()
                        .useNatives(SymbolicNativeMethods.natives())
                        .instructionFactory(instructionFactory)
                        .heapFactory(heapFactory)
                        .meta(PC, TrueSymbol.TT)
                        .meta(TERMINATION, false)
                        .meta(SC, 0)
                        .meta(S_PARAMETERS, new HashMap()));
        return vmBuilder;
    }

    public static JvmBuilder symbolicVmBuilder(final SymbInstructionFactory instructionFactory) {
        return symbolicVmBuilder(instructionFactory,
                new DepthFirstStateSearchFactory());
    }
}
