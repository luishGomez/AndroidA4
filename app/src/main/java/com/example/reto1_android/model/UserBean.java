package com.example.reto1_android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name="user")
public class UserBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Element(name="id")
    private Integer id;
    @Element(name="login")
    private String login;
    @Element(name="email")
    private String email;
    @Element(name="nombreCompleto")
    private String nombreCompleto;
    @Element(name="status")
    private UserStatus status;
    @Element(name="privilegio")
    private UserPrivilege privilegio;
    @Element(name="contrasenia")
    private String contrasenia;
    @Element(name="ultimoAcceso",required = false)
    private String ultimoAcceso;
    @Element(name="ultimoCambioContrasenia",required = false)
    private String ultimoCambioContrasenia;

    public UserBean() {
    }

    public UserBean(Integer id, String login, String email, String nombreCompleto, UserStatus status, UserPrivilege privilegio, String contrasenia, String ultimoAcceso, String ultimoCambioContrasenia) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.status = status;
        this.privilegio = privilegio;
        this.contrasenia = contrasenia;
        this.ultimoAcceso = ultimoAcceso;
        this.ultimoCambioContrasenia = ultimoCambioContrasenia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserPrivilege getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(UserPrivilege privilegio) {
        this.privilegio = privilegio;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(String ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public String getUltimoCambioContrasenia() {
        return ultimoCambioContrasenia;
    }

    public void setUltimoCambioContrasenia(String ultimoCambioContrasenia) {
        this.ultimoCambioContrasenia = ultimoCambioContrasenia;
    }

    @Override
    public String toString(){
        return nombreCompleto;
    }
}
