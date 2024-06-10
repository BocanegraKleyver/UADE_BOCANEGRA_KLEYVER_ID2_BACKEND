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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el puerto 3000
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<Carrito>> getAllCarritos() {
        List<Carrito> carritos = carritoService.getAllCarritos();
        return ResponseEntity.ok(carritos);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Carrito> obtenerCarritoPorUsuarioId(@PathVariable String usuarioId) {
        Optional<Carrito> carrito = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
        return carrito.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Carrito> crearCarrito(@PathVariable String usuarioId) {
        Optional<Carrito> carritoExistente = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoExistente.isPresent()) {
            // Si el usuario ya tiene un carrito, devolver ese carrito
            return ResponseEntity.ok(carritoExistente.get());
        } else {
            // Si el usuario no tiene carrito, crear uno nuevo
            Carrito carritoNuevo = carritoService.crearCarrito(usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(carritoNuevo);
        }
    }

    @PostMapping("/{usuarioId}/carritoProducto/producto")
    public ResponseEntity<Carrito> agregarProductosAlCarrito(@PathVariable String usuarioId, @RequestBody List<String> productosIds) {
        Optional<Carrito> carritoOptional = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            // Lógica para agregar productos al carrito
            carritoService.agregarProductosAlCarrito(carrito, productosIds);
            return ResponseEntity.ok(carrito);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    @DeleteMapping("/{usuarioId}/carritoProducto/producto/{productoId}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable String usuarioId, @PathVariable String productoId) {
        // Llamar al método adecuado en el servicio
        carritoService.eliminarProductoDelCarrito(usuarioId, productoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable String usuarioId) {
        carritoService.eliminarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
