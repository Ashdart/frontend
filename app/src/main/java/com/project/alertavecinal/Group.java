package com.project.alertavecinal;

public class Group {

    public String nombre, direccion, telefono, imagen, uid;

    public Group(){

    }

    public Group(String nombre, String uid) {
        this.nombre = nombre;
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
