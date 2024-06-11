package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pedido")
public class Pedido {

    @Id
    private String id; // ID del pedido

    private String usuarioId; // ID del usuario que realizó el pedido
    private String carritoId; // ID del carrito asociado al pedido
    private Date fecha; // Fecha del pedido
    private String estado; // Estado del pedido: pendiente, cancelado, confirmado
    private String nombreCliente; // Nombre del cliente
    private String apellidoCliente; // Apellido del cliente
    private String direccionCliente; // Dirección del cliente
    private String condicionIVA; // Condición de IVA del cliente
    private double importe; // Importe total del pedido

    // Constructor predeterminado
    public Pedido() {
    }

    // Constructor con todos los atributos
    public Pedido(String id, String usuarioId, String carritoId,Date fecha, String estado, String nombreCliente, String apellidoCliente, String direccionCliente, String condicionIVA, double importe) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.carritoId = carritoId;
        this.fecha = fecha;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccionCliente = direccionCliente;
        this.condicionIVA = condicionIVA;
        this.importe = importe;
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

    public void setCarritoId(String carritoId) {
        this.carritoId = carritoId;
    }

    public String getCarritoId() {
        return carritoId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getCondicionIVA() {
        return condicionIVA;
    }

    public void setCondicionIVA(String condicionIVA) {
        this.condicionIVA = condicionIVA;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
