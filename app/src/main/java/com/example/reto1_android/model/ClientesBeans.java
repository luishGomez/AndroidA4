package com.example.reto1_android.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Set;

@Root(name="clientes")
public class ClientesBeans {

    @ElementList(name="cliente", inline = true)
    private Set<ClienteBean> clientes;

    public Set<ClienteBean> getClientes() {
        return clientes;
    }

    public void setClientes(Set<ClienteBean> packs) {
        this.clientes = packs;
    }
}
