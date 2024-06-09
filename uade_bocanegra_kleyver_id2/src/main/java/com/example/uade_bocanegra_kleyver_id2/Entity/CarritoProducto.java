package com.example.uade_bocanegra_kleyver_id2.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carritoProducto")
public class CarritoProducto {

    @Id
    private String id;
    private String productoId;
    private int cantidad;
    private double precioCarritoDelProducto;

    public CarritoProducto() {}

    public CarritoProducto(String productoId, int cantidad, double precioCarritoDelProducto) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioCarritoDelProducto = precioCarritoDelProducto;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioCarritoDelProducto() {
        return precioCarritoDelProducto;
    }

    public void setPrecioCarritoDelProducto(double precioCarritoDelProducto) {
        this.precioCarritoDelProducto = precioCarritoDelProducto;
    }
}
