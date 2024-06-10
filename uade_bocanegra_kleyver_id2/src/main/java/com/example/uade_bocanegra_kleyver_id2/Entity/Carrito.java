package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carrito")
public class Carrito {

    @Id
    private String id;
    private String usuarioId;
    private List<CarritoProducto> carritoProductos;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private boolean activo;

    // Constructor
    public Carrito() {
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

    public List<CarritoProducto> getCarritoProductos() {
        return carritoProductos;
    }

    public void setCarritoProductos(List<CarritoProducto> carritoProductos) {
        this.carritoProductos = carritoProductos;
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

    // Método para calcular el precio total del carrito
    public double calcularPrecioTotal() {
        double precioTotal = 0.0;
        if (carritoProductos != null) {
            for (CarritoProducto producto : carritoProductos) {
                precioTotal += producto.getPrecioCarritoDelProducto();
            }
        }
        return precioTotal;
    }
}
