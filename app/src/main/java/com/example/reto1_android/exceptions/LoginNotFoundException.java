package com.example.reto1_android.exceptions;

public class LoginNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>loginNotFoundException</code> without
     * detail message.
     */
    public LoginNotFoundException() {
    }

    /**
     * Constructs an instance of <code>loginNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginNotFoundException(String msg) {
        super(msg);
    }
}