package com.example.reto1_android.encriptaciones;


import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.example.reto1_android.retrofit.ClienteAPIClient;
import com.example.reto1_android.retrofit.ClienteManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Esta clase permite encriptar y resumir datos.
 * @author Ricardo Peinado Lastra
 */
public class Encriptador {
    private static final Logger LOGGER =
            Logger.getLogger("Encriptador");
    private byte[] bytes=null;


    public Encriptador(){
        getPublicKey();
    }

    /**
     * Encripta en mensaje en claro que metas.
     * @param mensaje El mensaje en claro.
     * @return El mensaje encriptado.
     * @throws EncriptarException  Salta si ocurre un error al encriptar.
     */
    public String encriptar(String mensaje) throws EncriptarException{
        String encriptado=null;
        byte[] cipherText=null;

        try {


            EncodedKeySpec publicKeySpec = new  X509EncodedKeySpec(bytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = cipher.doFinal(mensaje.getBytes());

            encriptado= hexadecimal(cipherText);


            //encriptado=Base64.encodeBytes(vals);
            //encriptado=Base64.encode(vals, Base64.DEFAULT)

        } catch (Exception ex) {
            LOGGER.severe("ERROR AL ENCRIPTAR: "+ex.getMessage()+" "+ex.getCause());
            throw new EncriptarException(ex.getMessage());
        }
        LOGGER.severe("Clave retornada: "+cipherText.length+" "+encriptado);
        return encriptado;
    }



    private void getPublicKey() {
        ClienteManager clienteManager = ClienteAPIClient.getClientText();
        Call<String> call = clienteManager.getPublicKey();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                   // setClavePublica(Base64.decode(response.body(), Base64.DEFAULT));
                    setClavePublica(hexStringToByteArray(response.body()));
                }else{
                    LOGGER.severe("Error al leer la clave");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LOGGER.severe("Fallo al leer clave: "+t.getMessage());
            }
        });

    }
    public void setClavePublica(byte[] clave){
        LOGGER.severe("La calve cargada");
        this.bytes=clave;
    }

    /**
     * Convierte una lista de bytes a Hexadecimal.
     * @param resumen La colección de bytes.
     * @return Los bytes en hexadecimal.
     */
    static String hexadecimal(byte[] resumen) {
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1)
                HEX += "0";
            HEX += h;
        }
        return HEX.toUpperCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


}