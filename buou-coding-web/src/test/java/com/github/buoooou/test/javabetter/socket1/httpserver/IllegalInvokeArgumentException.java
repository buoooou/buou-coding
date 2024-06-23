package com.github.buoooou.test.javabetter.socket1.httpserver;

public class IllegalInvokeArgumentException extends RuntimeException {

    public IllegalInvokeArgumentException(String message) {
        super(message);
    }

    public IllegalInvokeArgumentException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}