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
import com.example.uade_bocanegra_kleyver_id2.Service.ProductoService;

@RestController
@RequestMapping("/api/carritoProducto")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el puerto 3000
public class CarritoProductoController {

    @Autowired
    private CarritoProductoService carritoProductoService;
    
    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<CarritoProducto> agregarProductoAlCarrito(@RequestBody CarritoProducto carritoProducto) {
        // Verificar si hay suficiente stock antes de agregar el producto al carrito
        boolean stockDisponible = productoService.verificarStockDisponible(carritoProducto.getProductoId(), carritoProducto.getCantidad());
        if (stockDisponible) {
            CarritoProducto savedCarritoProducto = carritoProductoService.agregarProductoAlCarrito(carritoProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCarritoProducto);
        } else {
            return ResponseEntity.badRequest().body(null); // Retornar un error 400 si no hay suficiente stock
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable String id) {
        carritoProductoService.eliminarProductoDelCarrito(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoProducto> obtenerProductoEnCarrito(@PathVariable String id) {
        Optional<CarritoProducto> carritoProducto = carritoProductoService.obtenerProductoEnCarrito(id);
        return carritoProducto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
