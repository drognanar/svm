package com.lexicalscope.svm.j.instruction.symbolic.symbols;

/** Class that contains methods generating symbolic inputs. */
public class SymbolFactory {
    /** Method that allows to create symbols in program runtime. */
    public static native int newIntSymbol();

    /** Method to select one option out of many. */
    public static native int selectState(int states);

    /** Returns the number of loop iterations. */
    public static native int loop();

    /** Calls a method with some arguments */
    public static native void invokeMethod(Object receiver, String methodName, Object[]... arguments);
}
