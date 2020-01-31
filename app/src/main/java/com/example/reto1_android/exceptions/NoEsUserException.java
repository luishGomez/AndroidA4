package com.example.reto1_android.exceptions;

public class NoEsUserException extends Exception {

    /**
     * Creates a new instance of <code>NoEsUserException</code> without detail
     * message.
     */
    public NoEsUserException() {
    }

    /**
     * Constructs an instance of <code>NoEsUserException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoEsUserException(String msg) {
        super(msg);
    }
}