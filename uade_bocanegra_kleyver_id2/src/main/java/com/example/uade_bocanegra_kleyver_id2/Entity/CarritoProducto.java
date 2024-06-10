package com.example.uade_bocanegra_kleyver_id2.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carritoProducto")
@CompoundIndex(def = "{'carritoId': 1, 'productoId': 1}", unique = true)
public class CarritoProducto {

    @Id
    private String id;
    @Indexed
    private String productoId;
    private int cantidad;
    private double precioCarritoDelProducto;
    @Indexed
    private String carritoId; // Id del carrito asociado

    // Getters y Setters
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

    public String getCarritoId() {
        return carritoId;
    }

    public void setCarritoId(String carritoId) {
        this.carritoId = carritoId;
    }
}
