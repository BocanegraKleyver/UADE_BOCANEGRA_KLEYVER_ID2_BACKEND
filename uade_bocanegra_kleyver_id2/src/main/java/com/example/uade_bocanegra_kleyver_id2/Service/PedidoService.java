package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.Pedido;
import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    CarritoService carritoService;

    @Autowired
    CacheService<Pedido> pedidoCacheService;
    
    @Autowired
    private UsuarioService usuarioService; 




    public Pedido guardarPedido(String carritoId) {
        // Obtener el carrito por su ID
        Carrito carrito = carritoService.obtenerCarritoPorIdcarrito(carritoId);
        
        if (carrito == null) {
            throw new RuntimeException("El carrito especificado no existe");
        }

        // Obtener el usuario asociado al carrito
        Usuario usuario = usuarioService.getUsuarioById(carrito.getUsuarioId());
 
        if (usuario == null) {
            throw new RuntimeException("No se pudo encontrar el usuario asociado al carrito");
        }

        // Crear un nuevo pedido con los datos del carrito
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuario.getId());
        pedido.setCarritoId(carritoId); // Asignar el carritoId al pedido
        pedido.setFecha(new Date());
        pedido.setEstado("pendiente"); // Estado inicial del pedido
        pedido.setNombreCliente(usuario.getNombre());
        pedido.setApellidoCliente(usuario.getApellido());
        pedido.setDireccionCliente(usuario.getDireccion());
        // Setear el tipo de IVA obtenido desde el frontend
        pedido.setCondicionIVA("Consumidor Final");
        // Obtener el importe total del carrito
        pedido.setImporte(carrito.getPrecioTotal());
        
        
        // Guardar el pedido en la base de datos
        return pedidoRepository.save(pedido);
    }
    /**
     * Método para obtener todos los pedidos
     */
    public List<Pedido> obtenerTodosPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Método para obtener un pedido por su ID
     */
    public Pedido obtenerPedidoPorId(String id) {
        Pedido pedido = pedidoCacheService.getFromCache("pedido:" + id);
        if (pedido == null) {
            pedido = pedidoRepository.findById(id).orElse(null);
            if (pedido != null) {
                pedidoCacheService.addToCache("pedido:" + id, pedido);
            }
        }
        return pedido;
    }

    /**
     * Método para obtener pedidos por ID de usuario
     */
    public List<Pedido> obtenerPedidosPorUsuarioId(String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
    /**
     * Método para actualizar un pedido
     */
    public Pedido actualizarPedido(Pedido pedido) {
        Pedido updatedPedido = pedidoRepository.save(pedido);
        pedidoCacheService.addToCache("pedido:" + pedido.getId(), updatedPedido);
        return updatedPedido;
    }

    /**
     * Método para eliminar un pedido por su ID
     */
    public void eliminarPedido(String id) {
        pedidoRepository.deleteById(id);
        pedidoCacheService.removeFromCache("pedido:" + id);
    }

    public List<Pedido> obtenerPedidosPorCarritoId(String carritoId) {
        return pedidoRepository.findByCarritoId(carritoId);
    }


    public void actualizarEstadoPedido(String pedidoId, String estado) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
        if (pedidoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            pedido.setEstado(estado);
            pedidoRepository.save(pedido);
            // Aquí podrías agregar la lógica para actualizar la caché si lo deseas
        } else {
            // Manejar el caso cuando el pedido no se encuentra
            System.out.println("No se encontró el pedido con ID: " + pedidoId);
        }
    }


}
