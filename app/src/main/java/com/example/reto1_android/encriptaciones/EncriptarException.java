package com.example.reto1_android.encriptaciones;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario
 */
public class EncriptarException extends Exception {

    /**
     * Creates a new instance of <code>EncriptarException</code> without detail
     * message.
     */
    public EncriptarException() {
    }

    /**
     * Constructs an instance of <code>EncriptarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EncriptarException(String msg) {
        super(msg);
    }
}
