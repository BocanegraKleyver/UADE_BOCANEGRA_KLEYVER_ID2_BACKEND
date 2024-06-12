package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Factura;
import com.example.uade_bocanegra_kleyver_id2.Repository.FacturaRepository;
import com.example.uade_bocanegra_kleyver_id2.Repository.PedidoRepository;

@Service
public class FacturaService {

    @Autowired
    FacturaRepository facturaRepository;


    @Autowired
    CarritoService carritoService;


    @Autowired
    PagoService pagoService;


    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoService pedidoService; // Esto debería eliminarse para evitar la dependencia circular




    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public List<Factura> findAllFacturas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> findFacturaById(String id) {
        return facturaRepository.findById(id);
    }

    public Factura saveFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    public void deleteFacturaById(String id) {
        facturaRepository.deleteById(id);
    }

    // Método para encontrar facturas por usuarioId
    public List<Factura> obtenerFacturasPorUsuarioId(String usuarioId) {
        return facturaRepository.findByUsuarioId(usuarioId);
    }

    // Método para encontrar facturas por pagoid
    public List<Factura> obtenerFacturasPorPagoId(String pagoId) {
        return facturaRepository.findByPagoId(pagoId);
    }


    public List<Factura> obtenerFacturasPorPedidoId(String pedidoId) {
        return facturaRepository.findByPedidoId(pedidoId);
    }


    // Método para marcar una factura como completada y actualizar el estado del pedido y pago
    public void confirmarCompra(String facturaId, String pedidoId, String pagoId) {
        Optional<Factura> facturaOptional = findFacturaById(facturaId);
        facturaOptional.ifPresent(factura -> {
            factura.setEstadoFactura("Completada");
            factura.setFechaFactura(new Date());
            factura.setPagoId(pagoId);
            saveFactura(factura);
        });
    
        // Actualizar el estado del pedido a completado
        pedidoService.actualizarEstadoPedido(pedidoId, "Completado");
    
        // Actualizar el estado del pago a completado
        pagoService.actualizarEstadoPago(pagoId, "Completado");

        // Cambiar el estado del carrito a inactivo y crear uno nuevo
        carritoService.marcarCarritoComoCerrado(facturaOptional.get().getUsuarioId());
        carritoService.crearCarrito(facturaOptional.get().getUsuarioId());
    }

    // Método para cancelar una factura y actualizar el estado del pedido y pago
    public void cancelarCompra(String facturaId, String pedidoId, String pagoId) {
        Optional<Factura> facturaOptional = findFacturaById(facturaId);
        facturaOptional.ifPresent(factura -> {
            factura.setEstadoFactura("Cancelada");
            factura.setFechaFactura(new Date());
            factura.setPagoId(pagoId);
            saveFactura(factura);
        });

        // Actualizar el estado del pedido a Cancelado
        pedidoService.actualizarEstadoPedido(pedidoId, "Cancelado");

        // Actualizar el estado del pago a Cancelado
        pagoService.actualizarEstadoPago(pagoId,"Cancelado");

                // Cambiar el estado del carrito a inactivo y crear uno nuevo
                carritoService.marcarCarritoComoCerrado(facturaOptional.get().getUsuarioId());
                carritoService.crearCarrito(facturaOptional.get().getUsuarioId());


    }

    // Otros métodos según sea necesario...
}
