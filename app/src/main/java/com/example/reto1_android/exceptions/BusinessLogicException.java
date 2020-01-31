package com.example.reto1_android.exceptions;

public class BusinessLogicException extends Exception {

    /**
     * Creates a new instance of <code>BusinessLogic</code> without detail
     * message.
     */
    public BusinessLogicException() {
    }

    /**
     * Constructs an instance of <code>BusinessLogic</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public BusinessLogicException(String msg) {
        super(msg);
    }
}