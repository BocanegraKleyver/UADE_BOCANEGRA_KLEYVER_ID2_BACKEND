package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sesion")
public class Sesion {

    @Id
    private String id;

    private String usuarioId;
    private Date fechaInicio;
    private Date fechaFin;
    private String categoriaSesion;
    private long categoriaSesionTiempo;

    // Constructores
    public Sesion() {
    }

    public Sesion(String usuarioId, Date fechaInicio, Date fechaFin,String categoriaSesion, long categoriaSesionTiempo) {
        this.usuarioId = usuarioId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.categoriaSesion = categoriaSesion;
        this.categoriaSesionTiempo = categoriaSesionTiempo;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCategoriaSesion() {
        return categoriaSesion;
    }

    public void setCategoriaSesion(String categoriaSesion) {
        this.categoriaSesion = categoriaSesion;
    }

    public long getCategoriaSesionTiempo() {
        return categoriaSesionTiempo;
    }

    public void setCategoriaSesionTiempo(long categoriaSesionTiempo) {
        this.categoriaSesionTiempo = categoriaSesionTiempo;
    }
}
