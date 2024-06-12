package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade_bocanegra_kleyver_id2.Entity.Factura;
import com.example.uade_bocanegra_kleyver_id2.Service.FacturaService;


@RestController
@RequestMapping("/api/factura")
@CrossOrigin(origins = "http://localhost:3000")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<Factura>> getAllFacturas() {
        List<Factura> facturas = facturaService.findAllFacturas();
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> getFacturaById(@PathVariable("id") String id) {
        Optional<Factura> factura = facturaService.findFacturaById(id);
        return factura.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Factura> createFactura(@RequestBody Factura factura) {
        Factura createdFactura = facturaService.saveFactura(factura);
        facturaService.confirmarCompra(createdFactura.getId(), createdFactura.getPedidoId(), createdFactura.getPagoId());

        return new ResponseEntity<>(createdFactura, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Factura> updateFactura(@PathVariable("id") String id, @RequestBody Factura factura) {
        Optional<Factura> facturaOptional = facturaService.findFacturaById(id);
        if (facturaOptional.isPresent()) {
            factura.setId(id); // Asegurar que el ID de la factura coincida
            Factura updatedFactura = facturaService.saveFactura(factura);
            return new ResponseEntity<>(updatedFactura, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteFactura(@PathVariable("id") String id) {
        try {
            // Obtener la factura que se va a eliminar
            Optional<Factura> facturaOptional = facturaService.findFacturaById(id);
            if (facturaOptional.isPresent()) {
                Factura factura = facturaOptional.get();
                String pedidoId = factura.getPedidoId();
                String pagoId = factura.getPagoId();
    
                // Cancelar la compra y actualizar el estado del pedido y pago
                facturaService.cancelarCompra(id, pedidoId, pagoId);
    
                // Eliminar la factura
                facturaService.deleteFacturaById(id);
                
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // Si la factura no existe
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
