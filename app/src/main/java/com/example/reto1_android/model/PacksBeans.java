package com.example.reto1_android.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name="packs")
public class PacksBeans {

    @ElementList(name="pack", inline = true)
    private List<PackBean> packs;

    public List<PackBean> getPacks() {
        return packs;
    }

    public void setPacks(List<PackBean> packs) {
        this.packs = packs;
    }
}
