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

import com.example.uade_bocanegra_kleyver_id2.Entity.Producto;
import com.example.uade_bocanegra_kleyver_id2.Service.ProductoService;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el puerto 3000
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable String id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> saveProducto(@RequestBody Producto producto) {
        Producto savedProducto = productoService.saveProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable String id, @RequestBody Producto producto) {
        Optional<Producto> existingProducto = productoService.getProductoById(id);
        if (existingProducto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        producto.setId(id); // Asignamos el ID recibido en la URL al producto
        Producto updatedProducto = productoService.saveProducto(producto);
        return ResponseEntity.ok(updatedProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
