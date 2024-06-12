package com.example.uade_bocanegra_kleyver_id2.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "factura")
public class Factura {

    @Id
    private String id; // ID de la factura
    private String usuarioId; // ID del usuario
    private String pagoId; // ID del pago
    private String pedidoId;// ID del pedido
    private Date fechaFactura; // Fecha de la factura
    private double importeTotal; // Monto total de la factura
    private String metodoPago; // Método de pago
    private String numeroTransaccion; // Número de transacción
    private String metodoEnvio; // Método de envío
    private String informacionContacto; // Información de contacto del cliente
    private String notasCliente; // Notas adicionales del cliente
    private double costoEnvio;
    private double impuestos;
    private double descuentos;

    // Datos del Cliente
    private String nombreUsuario;
    private String apellidoUsuario; // Nuevo campo para el apellido
    private String direccionUsuario;
    private String documentoIdentidadUsuario;
    private String emailUsuario;

    // Número de Factura y Serie
    private String numeroFactura;
    private String serieFactura;
    private String estadoFactura;

    // Datos del Vendedor
    private String nombreVendedor;
    private String direccionVendedor;
    private String identificacionFiscalVendedor;

    // Condiciones de Pago
    private String condicionesPago;

    // Notas o Comentarios Adicionales
    private String notasAdicionales;

    // Constructor
    public Factura() {
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

    public String getPagoId() {
        return pagoId;
    }

    public void setPagoId(String pagoId) {
        this.pagoId = pagoId;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
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

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    public double getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(double descuentos) {
        this.descuentos = descuentos;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        this.direccionUsuario = direccionUsuario;
    }

    public String getDocumentoIdentidadUsuario() {
        return documentoIdentidadUsuario;
    }

    public void setDocumentoIdentidadUsuario(String documentoIdentidadUsuario) {
        this.documentoIdentidadUsuario = documentoIdentidadUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getSerieFactura() {
        return serieFactura;
    }

    public void setSerieFactura(String serieFactura) {
        this.serieFactura = serieFactura;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getDireccionVendedor() {
        return direccionVendedor;
    }

    public void setDireccionVendedor(String direccionVendedor) {
        this.direccionVendedor = direccionVendedor;
    }

    public String getIdentificacionFiscalVendedor() {
        return identificacionFiscalVendedor;
    }

        public void setIdentificacionFiscalVendedor(String identificacionFiscalVendedor) {
            this.identificacionFiscalVendedor = identificacionFiscalVendedor;
        }
    
        public String getCondicionesPago() {
            return condicionesPago;
        }
    
        public void setCondicionesPago(String condicionesPago) {
            this.condicionesPago = condicionesPago;
        }
    
        public String getNotasAdicionales() {
            return notasAdicionales;
        }
    
        public void setNotasAdicionales(String notasAdicionales) {
            this.notasAdicionales = notasAdicionales;
        }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }
    }