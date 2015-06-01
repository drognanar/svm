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

        return DefaultNativeMethods.natives(symbolicNatives);
    }
}
