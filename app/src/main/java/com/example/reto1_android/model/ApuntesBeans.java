package com.example.reto1_android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.HashSet;
import java.util.Set;

@Root(name="apuntes")
public class ApuntesBeans {
    //@ElementList(name="apunte", inline = true)
    @ElementList( data=false, empty=true, inline=true, name="apunte",required=false)
    private
    Set<ApunteBean> apuntes=new HashSet<ApunteBean>();

    public ApuntesBeans() {
    }

    public ApuntesBeans(Set<ApunteBean> apuntes) {
        this.apuntes = apuntes;
    }

    public Set<ApunteBean> getApuntes() {
        return apuntes;
    }

    public void setApuntes(Set<ApunteBean> apuntes) {
        this.apuntes = apuntes;
    }
}
