package com.example.reto1_android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "apunte")
public class ApunteAndroid  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Element(name = "idApunte")
    private Integer idApunte;
    @Element(name = "titulo")
    private String titulo;
    @Element(name = "descripcion")
    private String descripcion;
    @Element(name = "archivo", required = false)
    private String archivo;
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

    public ApunteAndroid() {
    }

    public ApunteAndroid(Integer idApunte, String titulo, String descripcion, String archivo, String fechaValidacion, int likeCont, int dislikeCont, float precio, ClienteBean creador, MateriaBean materia) {
        this.idApunte = idApunte;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.archivo = archivo;
        this.fechaValidacion = fechaValidacion;
        this.likeCont = likeCont;
        this.dislikeCont = dislikeCont;
        this.precio = precio;
        this.creador = creador;
        this.materia = materia;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getFechaValidacion() {
        return fechaValidacion;
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
