package com.example.reto1_android.model;



import android.widget.ArrayAdapter;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Set;
import java.util.logging.Logger;


@Root(name="bbbbb", strict = false)
public class OfertaBeans {
    private static final Logger LOGGER = Logger.getLogger("hola buenas tardes");
    //@ElementList(name="apunte", inline = true)
    @ElementList(name = "oferta",inline = true, required = false)
    private Set<OfertaBean> ofertas;

    public OfertaBeans() {
    }

    public OfertaBeans(Set<OfertaBean> ofertas) {
        this.ofertas = ofertas;
    }

    public Set<OfertaBean> getOfertas() {
        if(ofertas==null)
            LOGGER.info("VAy vacios como no....");
        return ofertas;
    }

    public void setOfertas(Set<OfertaBean> ofertas) {
        this.ofertas = ofertas;
    }
}
