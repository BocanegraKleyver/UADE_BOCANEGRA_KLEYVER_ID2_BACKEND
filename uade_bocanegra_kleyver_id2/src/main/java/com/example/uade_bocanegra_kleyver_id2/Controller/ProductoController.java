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
import com.example.uade_bocanegra_kleyver_id2.Service.UsuarioActividadService;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

        @Autowired
    private UsuarioActividadService usuarioActividadService;

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
        try {
            Producto savedProducto = productoService.saveProducto(producto);
            usuarioActividadService.registrarActividad("sesionId", "Se creó un nuevo producto con ID: " + savedProducto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable String id, @RequestBody Producto producto) {
        try {
            Optional<Producto> existingProducto = productoService.getProductoById(id);
            if (existingProducto.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            producto.setId(id); // Asignamos el ID recibido en la URL al producto
            Producto updatedProducto = productoService.saveProducto(producto);
            usuarioActividadService.registrarActividad("sesionId", "Se actualizó el producto con ID: " + id);
            return ResponseEntity.ok(updatedProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        try {
            productoService.deleteProducto(id);
            usuarioActividadService.registrarActividad("sesionId", "Se eliminó el producto con ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
