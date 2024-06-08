package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document; // Agregar importación para ObjectId

@Document(collection = "sesion")
public class Sesion {

    @Id
    private String id;

    private ObjectId usuarioId; // Cambiar el tipo de dato a ObjectId
    private Date fechaInicio;
    private Date fechaFin;

    // Constructores
    public Sesion() {
    }

    public Sesion(ObjectId usuarioId, Date fechaInicio, Date fechaFin) { // Cambiar el tipo de dato del parámetro
        this.usuarioId = usuarioId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUsuarioId() { // Cambiar el tipo de retorno a ObjectId
        return usuarioId;
    }

    public void setUsuarioId(ObjectId usuarioId) { // Cambiar el tipo de dato del parámetro
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
}
