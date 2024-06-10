package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carritos")
public class Carrito {

    @Id
    private String id;
    private String usuarioId;
    private List<String> carritoProductoId; // Cambiado a List<String> para almacenar los IDs de los productos
    private Date fechaCreacion;
    private Date fechaModificacion;
    private boolean activo;
    private double precioTotal; // Nuevo campo para el precio total del carrito

    // Getters y Setters

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

    public List<String> getCarritoProductoId() {
        return carritoProductoId;
    }

    public void setCarritoProductoId(List<String> carritoProductoId) {
        this.carritoProductoId = carritoProductoId;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
