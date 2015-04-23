package com.lexicalscope.svm.j.instruction.symbolic.symbols;

/** Class that contains methods generating symbolic inputs. */
public class SymbolFactory {
    /** Method that allows to create symbols in program runtime. */
    public static native int newIntSymbol();

    /** Method to select one option out of many. */
    public static native int selectState(int states);

    /** Randomly chooses between true and false. */
    public static native boolean randomChoice();

    /** Returns a random element from a list */
    @SuppressWarnings("unchecked")
    public static native <T> T getArgument(T... arguments);
}
