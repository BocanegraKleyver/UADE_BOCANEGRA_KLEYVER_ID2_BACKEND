package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pago")
public class Pago {

    @Id
    private String id; // ID del pago
    private String usuarioId; // ID del usuario que realizó el pago
    private String pedidoId; // ID del pedido asociado al pago
    private Date fecha; // Fecha del pago
    private double importeTotal; // Monto del pago
    private String metodoPago; // Método de pago: efectivo, cta cte, tarjeta de crédito
    private String estadoPago; // Estado del pago: pendiente, completado, fallido, etc.
    private String numeroTransaccion; // Número de transacción asociado con el pago
    private String metodoEnvio; // Método de envío seleccionado por el cliente
    private String informacionContacto; // Información de contacto del cliente
    private String notasCliente; // Notas adicionales del cliente

    // Constructor predeterminado
    public Pago() {
    }

    // Constructor con todos los atributos
    public Pago(String id, String usuarioId, String pedidoId, Date fecha, double importeTotal, String metodoPago,
            String estadoPago, String numeroTransaccion, String metodoEnvio, String informacionContacto,
            String notasCliente) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pedidoId = pedidoId;
        this.fecha = fecha;
        this.importeTotal = importeTotal;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.numeroTransaccion = numeroTransaccion;
        this.metodoEnvio = metodoEnvio;
        this.informacionContacto = informacionContacto;
        this.notasCliente = notasCliente;
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

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public String getInformacionContacto() {
        return informacionContacto;
    }

    public void setInformacionContacto(String informacionContacto) {
        this.informacionContacto = informacionContacto;
    }

    public String getNotasCliente() {
        return notasCliente;
    }

    public void setNotasCliente(String notasCliente) {
        this.notasCliente = notasCliente;
    }
}
