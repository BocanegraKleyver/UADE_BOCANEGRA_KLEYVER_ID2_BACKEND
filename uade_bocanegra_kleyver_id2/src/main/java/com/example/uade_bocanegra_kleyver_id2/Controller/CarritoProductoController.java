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
import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoProductoService;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoService;

@RestController
@RequestMapping("/api/carritoProducto")
@CrossOrigin(origins = "http://localhost:3000")
public class CarritoProductoController {

    @Autowired
    private CarritoProductoService carritoProductoService;
    @Autowired
    private CarritoService carritoService;

    @PostMapping("/{usuarioId}/producto")
    public ResponseEntity<List<CarritoProducto>> agregarProductosAlCarrito(@PathVariable String usuarioId, @RequestBody List<CarritoProducto> productos) {
        Optional<Carrito> carritoOptional = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            List<CarritoProducto> savedProductos = carritoProductoService.agregarProductosAlCarrito(carrito, productos);

            // Actualizar el carrito en la base de datos con los nuevos productos
            carritoService.actualizarCarrito(carrito);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProductos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/producto/{productoId}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable String productoId) {
        carritoProductoService.eliminarProductoDelCarrito(productoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<CarritoProducto> obtenerProductoEnCarrito(@PathVariable String productoId) {
        Optional<CarritoProducto> carritoProducto = carritoProductoService.obtenerProductoEnCarrito(productoId);
        return carritoProducto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
