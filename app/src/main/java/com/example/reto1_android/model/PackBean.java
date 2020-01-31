package com.example.reto1_android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Root(name ="pack")
public class PackBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Element(name="idPack")
    private  Integer idPack;
    @Element(name="titulo")
    private  String titulo;
    @Element(name="descripcion")
    private  String descripcion;
    @Element(name="fechaModificacion", required = false)
    private  String fechaModificacion;
    @ElementList(name="apuntes", inline = true, required = false)
    private Set<ApuntePackBean> apuntes;

    public PackBean(){
        apuntes=new HashSet<ApuntePackBean>();
    }

    public PackBean(Integer idPack, String titulo, String descripcion, String fechaModificacion, Set<ApuntePackBean> apuntes) {
        this.idPack = idPack;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaModificacion = fechaModificacion;
        this.apuntes = apuntes;
    }

    public Integer getIdPack() {
        return idPack;
    }

    public void setIdPack(Integer idPack) {
        this.idPack = idPack;
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

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Set<ApuntePackBean> getApuntes() {
        return apuntes;
    }

    public void setApuntes(Set<ApuntePackBean> apuntes) {
        this.apuntes = apuntes;
    }
    public float getPrecio(){
        float precio = 0;
        if(getApuntes() != null){
            for(ApuntePackBean a : getApuntes()){
                precio += a.getPrecio();
            }
        }
        return precio;
    }
}
