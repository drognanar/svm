package com.lexicalscope.svm.vm;


public class TerminationException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
