package com.lexicalscope.svm.j.instruction.symbolic.symbols;

/** Class that contains methods generating symbolic inputs. */
public class SymbolFactory {
    /** Create an integer symbols while symbolically executing the program. */
    public static native int newIntSymbol();

    /** Create a boolean symbols while symbolically executing the program. */
    public static native boolean newBooleanSymbol();

    /** Method to select one option out of many. */
    public static native int selectState(int states);

    /** Randomly chooses between true and false. */
    public static native boolean randomChoice();

    /** Returns a random element from a list */
    public static native <T> T getArgument(T... arguments);
}
