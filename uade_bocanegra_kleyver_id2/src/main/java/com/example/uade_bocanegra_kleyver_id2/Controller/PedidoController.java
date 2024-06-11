package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.util.List;

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

import com.example.uade_bocanegra_kleyver_id2.Entity.Pedido;
import com.example.uade_bocanegra_kleyver_id2.Service.PedidoService;

@RestController
@RequestMapping("/api/pedido")
@CrossOrigin(origins = "http://localhost:3000") // Permitir solicitudes desde el frontend en el puerto 3000
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable String id) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorUsuarioId(@PathVariable String usuarioId) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuarioId(usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping("/crear/{carritoId}")
    public ResponseEntity<Pedido> crearPedido(@PathVariable String carritoId) {
        Pedido nuevoPedido = pedidoService.guardarPedido(carritoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable String id, @RequestBody Pedido pedido) {
        Pedido pedidoExistente = pedidoService.obtenerPedidoPorId(id);
        if (pedidoExistente != null) {
            pedido.setId(id);
            Pedido pedidoActualizado = pedidoService.actualizarPedido(pedido);
            return ResponseEntity.ok(pedidoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable String id) {
        // No es necesario verificar si el pedido existe antes de eliminarlo,
        // ya que el servicio se encargará de manejarlo correctamente.
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}