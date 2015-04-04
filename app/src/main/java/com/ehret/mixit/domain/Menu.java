package com.ehret.mixit.domain;

public class Menu {
    private int colorResource;
    private int label;
    private int id;

    public int getColorResource() {
        return colorResource;
    }

    public Menu setColorResource(int colorResource) {
        this.colorResource = colorResource;
        return this;
    }

    public int getLabel() {
        return label;
    }

    public Menu setLabel(int label) {
        this.label = label;
        return this;
    }

    public int getId() {
        return id;
    }

    public Menu setId(int id) {
        this.id = id;
        return this;
    }
}
