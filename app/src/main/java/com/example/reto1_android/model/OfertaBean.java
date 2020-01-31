package com.example.reto1_android.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Set;




@Root(name="oferta",strict = false)
public class OfertaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Element(name="idOferta")
    private  Integer idOferta;
    @Element(name = "titulo")
    private  String titulo;
    @Element(name = "fechaInicio")
    private  String fechaInicio;
    @Element(name = "fechaFin")
    private  String fechaFin;
    @Element(name = "rebaja")
    private  Float rebaja;
    @ElementList(data=false, empty=true, inline=true, name="packs",required=false)
    private  Set<PackBean> packs;

    public OfertaBean() {
        idOferta=0;
    }

    public OfertaBean(Integer idOferta, String titulo, String fechaInicio, String fechaFin, Set<PackBean> packs, Float rebaja) {
        this.idOferta = idOferta;
        this.titulo = titulo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.packs = packs;
        this.rebaja = rebaja;
    }

    public Integer getIdOferta() {
        return idOferta;
    }

    public void setTitulo(String titulo) {
        this.titulo=titulo;
    }
    public void setRebaja(Float rebaja) {
        this.rebaja=rebaja;
    }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio=fechaInicio; }
    public void setFechaFin(String fechaFin) {
        this.fechaFin=fechaFin;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getFechaInicio() {
        return fechaInicio;
    }
    public String getFechaFin() {
        return fechaFin;
    }
    public Float getRebaja() {
        return rebaja;
    }

    @Override
    public String toString() {
        return titulo+" "+fechaInicio+" "+fechaFin+" "+rebaja.toString();
    }

    public String tofrase() {
        return "Titulo: "+titulo+" "+"\n\n" +
                "Fecha Inicio: "+fechaInicio.substring(0,10)+"  Fecha Fin: "+fechaFin.substring(0,10)+" \n" +
                "Rebaja: "+rebaja.toString();
    }



}
