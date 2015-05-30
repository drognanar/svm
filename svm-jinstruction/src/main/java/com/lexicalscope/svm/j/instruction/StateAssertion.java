package com.lexicalscope.svm.j.instruction;

import com.lexicalscope.svm.vm.TerminationException;

/**
 * With non deterministic behaviour some operations cannot be executed.
 * For example the program might try to call null.method().
 * In this case crash one of svm states and not the svm itself.
 */
public class StateAssertion {
    public static void assertState(boolean predicate, String error) {
        if (!predicate) {
            throw new TerminationException();
        }
    }
}
