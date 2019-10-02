package com.project.alertavecinal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Alerta {

    public String mensaje, userId, userName, direccion, destUserId, descripcion, groupId, imagen;
    public Date fechaHora;

    public Alerta() {
    }

    public Alerta(String Mensaje, String userId, String userName, String direccion, String destUserId, String descripcion, String groupId, Date fechaHora, String Imagen) {

        this.mensaje = Mensaje;
        this.userName = userName;
        this.direccion = direccion;
        this.userId = userId;
        this.destUserId = destUserId;
        this.descripcion = descripcion;
        this.groupId = groupId;
        this.fechaHora = fechaHora;
        this.imagen = Imagen;

    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    public String getGroupId() {return groupId;}
    public void setGroupId(String groupId) {this.groupId = groupId;}

    public Date getFechaHora() {return fechaHora;}
    public void setFechaHora(Date fechaHora) {this.fechaHora = fechaHora;}

    public String getFormatTime() {
        return new SimpleDateFormat("dd/MM/yy HH:mm").format(this.fechaHora);
        //return DateFormat.getDateInstance().format(this.fechaHora);
    }


}
