package com.example.reto1_android.exceptions;

public class PasswordWrongException extends Exception {

    /**
     * Creates a new instance of <code>passwordWrongException</code> without
     * detail message.
     */
    public PasswordWrongException() {
    }

    /**
     * Constructs an instance of <code>passwordWrongException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PasswordWrongException(String msg) {
        super(msg);
    }
}