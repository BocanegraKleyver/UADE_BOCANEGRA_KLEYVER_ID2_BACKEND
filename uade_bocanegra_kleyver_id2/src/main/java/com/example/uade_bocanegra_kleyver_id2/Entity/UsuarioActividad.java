package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarioActividad")
public class UsuarioActividad {

    @Id
    private String id;

    private String sesionId;
    
    private String actividad;
    
    private Date fecha;

    public UsuarioActividad(){}
    
    public UsuarioActividad(String id, String sesionId, String actividad, Date fecha) {
        this.id = id;
        this.sesionId = sesionId;
        this.actividad = actividad;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public String getSesionId() {
        return sesionId;
    }

    public String getActividad() {
        return actividad;
    }

    public Date getFecha() {
        return fecha;
    }

    // Constructores, getters y setters

    public void setId(String id) {
        this.id = id;
    }

    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
