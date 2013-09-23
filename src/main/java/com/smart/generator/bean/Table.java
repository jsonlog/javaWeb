package com.smart.generator.bean;

public class Table {

    private String name;
    private String pk;

    public Table(String name, String pk) {
        this.name = name;
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
