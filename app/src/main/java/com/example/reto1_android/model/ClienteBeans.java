package com.example.reto1_android.model;



import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


@Root(name="clientes")
public class ClienteBeans {
    private static final Logger LOGGER = Logger.getLogger("ClienteBeans");

    @ElementList(name = "cliente",inline = true, required = false)
    private Set<ClienteBean> clientes;

    public ClienteBeans() {
        clientes=new HashSet<ClienteBean>();
    }

    public ClienteBeans(Set<ClienteBean> clientes) {
        this.clientes = clientes;
    }

    public Set<ClienteBean> getClientes() {return clientes;}

    public void setClientes(Set<ClienteBean> clientes) {
        this.clientes = clientes;
    }
}
