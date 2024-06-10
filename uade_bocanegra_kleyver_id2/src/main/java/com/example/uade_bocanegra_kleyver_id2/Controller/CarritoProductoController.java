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

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Request.CarritoProductoRequest;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoProductoService;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoService;

@RestController
@RequestMapping("/api/carritoProducto")
@CrossOrigin(origins = "http://localhost:3000")
public class CarritoProductoController {

    @Autowired
    private CarritoProductoService carritoProductoService;

    @Autowired // Agrega esta anotación para que Spring inyecte el servicio automáticamente
    private CarritoService carritoService; 

    @GetMapping
    public ResponseEntity<List<CarritoProducto>> getAllCarritoProducto() {
        List<CarritoProducto> carritoProductos = carritoProductoService.getAllCarritoProducto();
        return ResponseEntity.ok(carritoProductos);
    }
    


    @PostMapping("/{carritoId}/producto")
    public ResponseEntity<?> agregarProductoAlCarritoProducto(@PathVariable String carritoId, @RequestBody CarritoProductoRequest productoRequest) {
        try {
            CarritoProducto savedProducto = carritoProductoService.agregarProductoAlCarrito(carritoId, productoRequest);

            carritoService.agregarIdsCarritoProductoAlCarrito(carritoId, List.of(savedProducto.getId()));

            // Log para rastrear la acción de agregar un producto al carritoProducto
            System.out.println("carritoProducto agregado al carrito. ID del carrito: " + carritoId + ", ID del carritoProducto: " + savedProducto.getId());

            // Actualizar el carrito con los IDs de los productos
            carritoService.agregarIdsCarritoProductoAlCarrito(carritoId, List.of(savedProducto.getId())); // Pasar el ID como lista

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/producto/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarritoProducto(@PathVariable String id) {
        carritoProductoService.eliminarProductoDelCarrito(id);

        // Log para rastrear la acción de eliminar un producto del carritoProducto
        System.out.println("carritoProducto eliminado del carrito. ID del carritoProducto: " + id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<CarritoProducto> obtenerProductoEnCarritoProducto(@PathVariable String id) {
        Optional<CarritoProducto> carritoProducto = carritoProductoService.obtenerProductoEnCarrito(id);
        return carritoProducto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
