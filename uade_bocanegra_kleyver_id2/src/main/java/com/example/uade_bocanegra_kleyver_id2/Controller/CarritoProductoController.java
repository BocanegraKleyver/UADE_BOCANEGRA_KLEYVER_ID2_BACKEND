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
        carritoProductoService.eliminarCarritoProductoDelCarrito(id);

        // Log para rastrear la acción de eliminar un producto del carritoProducto
        System.out.println("carritoProducto eliminado del carrito. ID del carritoProducto: " + id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<CarritoProducto> obtenerProductoEnCarritoProducto(@PathVariable String id) {
        Optional<CarritoProducto> carritoProducto = carritoProductoService.obtenerProductoEnCarrito(id);
        return carritoProducto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoProducto> getCarritoProductoById(@PathVariable("id") String id) {
        Optional<CarritoProducto> carritoProducto = carritoProductoService.getCarritoProductoById(id);
        if (carritoProducto.isPresent()) {
            return ResponseEntity.ok(carritoProducto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/carrito/{carritoId}")
    public ResponseEntity<List<CarritoProducto>> getCarritoProductoByCarritoId(@PathVariable String carritoId) {
        List<CarritoProducto> carritoProductos = carritoProductoService.getCarritoProductoByCarritoId(carritoId);
        return ResponseEntity.ok(carritoProductos);
    }

    @PutMapping("/{carritoProductoId}")
    public ResponseEntity<CarritoProducto> modificarCantidadProductoEnCarrito(@PathVariable String carritoProductoId, @RequestBody CarritoProductoRequest carritoProductoRequest) {
        System.out.println("Modificando cantidad del producto en el carrito. ID del carritoProducto: " + carritoProductoId + ", Nueva cantidad: " + carritoProductoRequest.getCantidad());
        CarritoProducto carritoProducto = carritoProductoService.modificarCantidadProductoEnCarrito(carritoProductoId, carritoProductoRequest.getCantidad());
    
        // Actualizar el carrito después de modificar la cantidad del producto
        carritoService.actualizarCarritoDespuesDeModificarCantidadProducto(carritoProducto.getCarritoId());
    
        return ResponseEntity.ok(carritoProducto);
    }

    @GetMapping("/carritoProducto/{carritoProductoId}")
public ResponseEntity<CarritoProducto> obtenerCarritoProductoPorId(@PathVariable String carritoProductoId) {
    Optional<CarritoProducto> carritoProducto = carritoProductoService.obtenerCarritoProductoPorId(carritoProductoId);
    return carritoProducto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
}

@DeleteMapping("/delete/{carritoProductoId}")
    public ResponseEntity<Void> eliminarCarritoProducto(@PathVariable String carritoProductoId) {
        carritoProductoService.eliminarCarritoProductoDelCarrito(carritoProductoId);

        // Después de eliminar el CarritoProducto, actualiza el Carrito
        carritoService.actualizarCarritoDespuesDeEliminarProducto(carritoProductoId);

        return ResponseEntity.noContent().build();
    }

}
