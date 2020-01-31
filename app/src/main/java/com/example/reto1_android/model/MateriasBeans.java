package com.example.reto1_android.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Set;

@Root(name="materias")
public class MateriasBeans {
    @ElementList(name="materia", inline = true)
    private
    Set<MateriaBean> materias;

    public MateriasBeans(Set<MateriaBean> materias) {
        this.setMaterias(materias);
    }

    public MateriasBeans() {
    }


    public Set<MateriaBean> getMaterias() {
        return materias;
    }

    public void setMaterias(Set<MateriaBean> materias) {
        this.materias = materias;
    }
}
