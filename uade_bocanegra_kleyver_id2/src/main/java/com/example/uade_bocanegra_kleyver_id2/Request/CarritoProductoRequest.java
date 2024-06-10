package com.example.uade_bocanegra_kleyver_id2.Request;

public class CarritoProductoRequest {

    private String productoId;
    private int cantidad;

    // Getters y setters

    public String getProductoId() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}