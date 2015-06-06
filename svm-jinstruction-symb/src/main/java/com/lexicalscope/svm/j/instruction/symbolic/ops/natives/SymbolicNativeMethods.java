package com.lexicalscope.svm.j.instruction.symbolic.ops.natives;

import com.lexicalscope.svm.j.natives.DefaultNativeMethods;
import com.lexicalscope.svm.j.natives.NativeMethodDef;
import com.lexicalscope.svm.j.natives.NativeMethods;

import java.util.ArrayList;

public class SymbolicNativeMethods {
    public static NativeMethods natives() {
        ArrayList<NativeMethodDef> symbolicNatives = new ArrayList<>();
        symbolicNatives.addAll(DefaultNativeMethods.nativeMethodList());
        symbolicNatives.add(new Symbolic_newArray("newCharArraySymbol", "()[C"));
        symbolicNatives.add(new Symbolic_Java_lang_system_arraycopy());
        symbolicNatives.add(new Symbolic_selectState());
        symbolicNatives.add(new Symbolic_getArgument());
        symbolicNatives.add(new Symbolic_randomChoice());
        symbolicNatives.add(new Symbolic_newSymbol("newIntSymbol", "()I"));
        symbolicNatives.add(new Symbolic_newSymbol("newBooleanSymbol", "()Z"));
        symbolicNatives.add(new Symbolic_getPassedParameter());
        symbolicNatives.add(new Symbolic_countParameters());
        symbolicNatives.add(new Symbolic_passParameter());

        return DefaultNativeMethods.natives(symbolicNatives);
    }
}
