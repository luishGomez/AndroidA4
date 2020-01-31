package com.example.reto1_android.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="cliente")
public class ClienteBean extends UserBean implements Serializable {
    @Element(name="saldo", required = false)
    private float saldo;
    @Element(name="foto" ,required = false)
    private String foto;


    public ClienteBean() {
    }

    public ClienteBean(Integer id, String login, String email, String nombreCompleto, UserStatus status, UserPrivilege privilegio, String contrasenia, String ultimoAcceso, String ultimoCambioContrasenia, float saldo, String foto) {
        super(id, login, email, nombreCompleto, status, privilegio, contrasenia, ultimoAcceso, ultimoCambioContrasenia);
        this.saldo = saldo;
        this.foto = foto;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
