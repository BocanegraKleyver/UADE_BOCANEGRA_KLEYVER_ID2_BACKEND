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

import com.example.uade_bocanegra_kleyver_id2.Entity.Pago;
import com.example.uade_bocanegra_kleyver_id2.Service.PagoService;

@RestController
@RequestMapping("/api/pago")
@CrossOrigin(origins = "http://localhost:3000")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> getAllPagos() {
        List<Pago> pagos = pagoService.getAllPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPagoById(@PathVariable String id) {
        Optional<Pago> pago = pagoService.getPagoById(id);
        return pago.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pago> savePago(@RequestBody Pago pago) {
        Pago savedPago = pagoService.savePago(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> updatePago(@PathVariable String id, @RequestBody Pago pago) {
        Optional<Pago> existingPago = pagoService.getPagoById(id);
        if (existingPago.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pago.setId(id); // Asignamos el ID recibido en la URL al pago
        Pago updatedPago = pagoService.savePago(pago);
        return ResponseEntity.ok(updatedPago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable String id) {
        pagoService.deletePago(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar método de pago
    @PutMapping("/{id}/metodo-pago")
    public ResponseEntity<Pago> actualizarMetodoPago(@PathVariable String id, @RequestBody String metodoPago) {
        try {
            Pago updatedPago = pagoService.actualizarMetodoPago(id, metodoPago);
            return ResponseEntity.ok(updatedPago);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar método de envío
    @PutMapping("/{id}/metodo-envio")
    public ResponseEntity<Pago> actualizarMetodoEnvio(@PathVariable String id, @RequestBody String metodoEnvio) {
        try {
            Pago updatedPago = pagoService.actualizarMetodoEnvio(id, metodoEnvio);
            return ResponseEntity.ok(updatedPago);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar notas del cliente
    @PutMapping("/{id}/notas-cliente")
    public ResponseEntity<Pago> actualizarNotasCliente(@PathVariable String id, @RequestBody String notasCliente) {
        try {
            Pago updatedPago = pagoService.actualizarNotasCliente(id, notasCliente);
            return ResponseEntity.ok(updatedPago);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
