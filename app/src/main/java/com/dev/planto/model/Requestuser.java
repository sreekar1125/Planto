package com.dev.planto.model;

public class Requestuser {
    private String Name;

    public Requestuser(String name) {
        Name = name;
    }

    public Requestuser() {
    }

    public String getName() {
        return Name;
    }
    public void setName(String name){
        Name=name;
    }
}
