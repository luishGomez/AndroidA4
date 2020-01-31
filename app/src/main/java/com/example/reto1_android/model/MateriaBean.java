package com.example.reto1_android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
@Root(name="materia")
public class MateriaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Element(name="idMateria")
    private  Integer idMateria;
    @Element(name="titulo")
    private  String titulo;
    @Element(name="descripcion")
    private  String descripcion;

    public MateriaBean() {
    }

    public MateriaBean(Integer idMateria, String titulo, String descripcion) {
        this.idMateria = idMateria;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
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
    @Override
    public String toString(){
        return titulo;
    }
}
