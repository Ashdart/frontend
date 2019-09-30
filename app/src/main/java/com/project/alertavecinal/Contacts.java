package com.project.alertavecinal;

public class Contacts {

    public String nombre, direccion, telefono, imagen, uid;

    public Contacts(){

    }

    public Contacts(String apellido, String nombre, String direccion, String telefono, String imagen, String uid) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.imagen = imagen;
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
