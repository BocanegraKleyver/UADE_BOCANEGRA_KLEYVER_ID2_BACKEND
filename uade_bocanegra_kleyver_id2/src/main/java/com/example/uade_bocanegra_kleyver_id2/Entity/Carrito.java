package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "carrito")
public class Carrito {

    @Id
    private String id;
    private String usuarioId;
    private String estado; // activo, abandonado, comprado, etc.
    private Date fechaCreacion;
    private Date fechaModificacion;
    private List<CarritoProducto> carritoProducto = new ArrayList<>();

    public Carrito() {}

    public Carrito(String usuarioId, String estado, Date fechaCreacion, Date fechaModificacion, List<CarritoProducto> carritoProducto) {
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.carritoProducto = new ArrayList<>(); // Inicializar la lista aquí
    }
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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


    public List<CarritoProducto> getProductos() {
        if (carritoProducto == null) {
            carritoProducto = new ArrayList<>();
        }
        return carritoProducto;
    }

    
    public void setProductos(List<CarritoProducto> carritoProducto) {
        this.carritoProducto = carritoProducto;
    }


}


