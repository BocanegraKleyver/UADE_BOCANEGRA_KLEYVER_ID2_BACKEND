package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "producto")
public class Producto {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private double precioAnterior;
    private int cantidad;
    private String imagen;
    private String video;
    private List<String> comentarios;
    private double descuento;
    private Date fechaCarga;
    private Date fechaModificacion;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(String nombre, String descripcion, double precio,double precioAnterior, int cantidad, String imagen, String video,
            List<String> comentarios, double descuento, Date fechaCarga, Date fechaModificacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioAnterior = precioAnterior;
        this.cantidad = cantidad;
        this.imagen = imagen;
        this.video = video;
        this.comentarios = comentarios;
        this.descuento = descuento;
        this.fechaCarga = fechaCarga;
        this.fechaModificacion = fechaModificacion;
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioAnterior() {
        return precioAnterior;
    }

    public void setPrecioAnterior(double precioAnterior) {
        this.precioAnterior = precioAnterior;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<String> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<String> comentarios) {
        this.comentarios = comentarios;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }



    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }
}
