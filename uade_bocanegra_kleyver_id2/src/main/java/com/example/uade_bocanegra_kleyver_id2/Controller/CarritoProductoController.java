package com.example.uade_bocanegra_kleyver_id2.Controller;

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

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoProductoService;

@RestController
@RequestMapping("/api/carrito/{usuarioId}/carritoProducto")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el puerto 3000
public class CarritoProductoController {

    @Autowired
    private CarritoProductoService carritoProductoService;

    @PostMapping("/producto")
    public ResponseEntity<CarritoProducto> agregarProductoAlCarrito(@PathVariable String usuarioId, @RequestBody CarritoProducto carritoProducto) {
        // Lógica para agregar el producto al carrito
        CarritoProducto savedCarritoProducto = carritoProductoService.agregarProductoAlCarrito(carritoProducto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCarritoProducto);
    }

    @DeleteMapping("/producto/{productoId}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable String usuarioId, @PathVariable String productoId) {
        // Lógica para eliminar el producto del carrito
        carritoProductoService.eliminarProductoDelCarrito(productoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<CarritoProducto> obtenerProductoEnCarrito(@PathVariable String usuarioId, @PathVariable String productoId) {
        // Lógica para obtener el producto del carrito
        Optional<CarritoProducto> carritoProducto = carritoProductoService.obtenerProductoEnCarrito(productoId);
        return carritoProducto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
