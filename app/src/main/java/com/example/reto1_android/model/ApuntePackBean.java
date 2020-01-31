package com.example.reto1_android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Root(name = "apuntes")
public class ApuntePackBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Element(name = "idApunte")
    private Integer idApunte;
    @Element(name = "titulo")
    private String titulo;
    @Element(name = "descripcion")
    private String descripcion;
    @ElementArray(name = "archivo", required = false)
    private byte[] archivo;
    @Element(name = "fechaValidacion", required = false)
    private String fechaValidacion = null;
    @Element(name = "likeCont")
    private int likeCont;
    @Element(name = "dislikeCont")
    private int dislikeCont;
    @Element(name = "precio")
    private float precio;
    @Element(name = "creador")
    private ClienteBean creador;
    @Element(name = "materia")
    private MateriaBean materia;

    public ApuntePackBean() {
    }

    public ApuntePackBean(Integer idApunte, String titulo, String descripcion, byte[] archivo, Date fechaValidacion, int likeCont, int dislikeCont, float precio, ClienteBean creador, MateriaBean materia) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        this.idApunte = idApunte;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.fechaValidacion = fechaValidacion.toString();
        this.likeCont = likeCont;
        this.dislikeCont = dislikeCont;
        this.precio = precio;
        this.creador = creador;
        this.materia = materia;
    }

    public Integer getIdApunte() {
        return idApunte;
    }

    public void setIdApunte(Integer idApunte) {
        this.idApunte = idApunte;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getFechaValidacion() {
        /*
        Date resultado = null;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss");
            resultado = formatter.parse(fechaValidacion);

        } catch (Exception e) {

        }
        return resultado;
        */
        return this.fechaValidacion;

    }

    public void setFechaValidacion(String fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public int getLikeCont() {
        return likeCont;
    }

    public void setLikeCont(int likeCont) {
        this.likeCont = likeCont;
    }

    public int getDislikeCont() {
        return dislikeCont;
    }

    public void setDislikeCont(int dislikeCont) {
        this.dislikeCont = dislikeCont;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public ClienteBean getCreador() {
        return creador;
    }

    public void setCreador(ClienteBean creador) {
        this.creador = creador;
    }

    public MateriaBean getMateria() {
        return materia;
    }

    public void setMateria(MateriaBean materia) {
        this.materia = materia;
    }
}
