package com.example.peidosaguacelica106.Datos;

import java.util.List;

public class DatosPadre {

    private String sectionName;
    private List<Datos> sectioniten;

    public DatosPadre(String sectionName, List<Datos> sectioniten) {
        this.sectionName = sectionName;
        this.sectioniten = sectioniten;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<Datos> getSectioniten() {
        return sectioniten;
    }

    public void setSectioniten(List<Datos> sectioniten) {
        this.sectioniten = sectioniten;
    }

    @Override
    public String toString() {
        return "DatosPadre{" +
                "sectionName='" + sectionName + '\'' +
                ", sectioniten=" + sectioniten +
                '}';
    }
}
