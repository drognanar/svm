package com.lexicalscope.svm.j.instruction.symbolic.symbols;

import java.util.Iterator;

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

    public static native void passParameter(Object receiver, Class<?> obClass, Object ob);

    private static native int countParameters(Object receiver, Class<?> klass);

    private static native Object getPassedParameter(Object receiver, Class<?> klass, int index);

    public static Object getPassedParameter(Object receiver, Class<?> klass) {
        int parameterIndex = selectState(countParameters(receiver, klass));
        return getPassedParameter(receiver, klass, parameterIndex);
    }

    public static <T> Iterable<T> getIterable(final Object receiver, final Class<T> klass) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new SymbolicIterator<>(receiver, klass);
            }
        };
    }

    private static class SymbolicIterator<T> implements Iterator<T> {
        private final Object receiver;
        private final Class<T> klass;
        private final int maxCount;
        private int count;

        public SymbolicIterator(Object receiver, Class<T> klass) {
            this.receiver = receiver;
            this.klass = klass;
            this.count = 0;
            this.maxCount = countParameters(receiver, klass);
        }

        @Override
        public boolean hasNext() {
            return count < maxCount;
        }

        @Override
        public T next() {
            T passedParameter = (T) getPassedParameter(receiver, klass, count);
            count++;
            return passedParameter;
        }

        @Override
        public void remove() {
        }
    }
}
