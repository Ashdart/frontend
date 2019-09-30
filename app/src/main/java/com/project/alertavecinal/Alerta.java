package com.project.alertavecinal;

import java.util.Calendar;
import java.util.Date;

public class Alerta {

    public String mensaje, userId, direccion, destUserId, descripcion, groupId;
    public Date fechaHora;

    public Alerta() {
    }

    public Alerta(String Mensaje, String userId, String direccion, String destUserId, String descripcion, String groupId, Date fechaHora) {

        this.mensaje = Mensaje;
        this.direccion = direccion;
        this.userId = userId;
        this.destUserId = destUserId;
        this.descripcion = descripcion;
        this.groupId = groupId;
        this.fechaHora = fechaHora;

    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {return groupId;}
    public void setGroupId(String groupId) {this.groupId = groupId;}

    public Date getFechaHora() {return fechaHora;}
    public void setFechaHora(Date fechaHora) {this.fechaHora = fechaHora;}

}
