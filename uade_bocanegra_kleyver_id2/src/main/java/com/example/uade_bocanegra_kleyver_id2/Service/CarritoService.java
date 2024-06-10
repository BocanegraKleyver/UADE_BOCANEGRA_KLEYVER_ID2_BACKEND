package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CacheService<Carrito> carritoCacheService;

    public List<Carrito> obtenerCarritosPorUsuarioId(String usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public Carrito crearCarrito(String usuarioId) {
        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);
        carrito.setActivo(true); // Modificado para usar booleano
        carrito.setFechaCreacion(new Date());
        carrito.setFechaModificacion(new Date());
        Carrito savedCarrito = carritoRepository.save(carrito);
        
        // Aquí definimos la clave de la caché usando el ID del usuario
        String cacheKey = "carrito:" + usuarioId;
        carritoCacheService.addToCache(cacheKey, savedCarrito);
        
        return savedCarrito;
    }
    
    public Carrito actualizarCarrito(Carrito carrito) {
        carrito.setFechaModificacion(new Date());
        Carrito updatedCarrito = carritoRepository.save(carrito);
        carritoCacheService.addToCache("carrito:" + carrito.getUsuarioId(), updatedCarrito);
        return updatedCarrito;
    }

    public Optional<Carrito> obtenerCarritoPorUsuarioId(String usuarioId) {
        // Obtener la lista de carritos asociados al usuario
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);

        // Si la lista está vacía, retornar Optional.empty()
        if (carritos.isEmpty()) {
            return Optional.empty();
        }

        // Si hay al menos un carrito, tomar el primero
        Carrito carrito = carritos.get(0);

        // Agregar el carrito a la caché
        carritoCacheService.addToCache("carrito:" + usuarioId, carrito);

        // Retornar el carrito envuelto en un Optional
        return Optional.of(carrito);
    }

    public void marcarCarritoComoCerrado(String usuarioId) {
        // Obtener la lista de carritos asociados al usuario
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        // Iterar sobre los carritos y marcarlos como cerrados
        for (Carrito carrito : carritos) {
            carrito.setActivo(false); // Modificado para usar booleano
            carritoRepository.save(carrito);
        }
    }

    public void eliminarCarrito(String usuarioId) {
        String cacheKey = "carrito:" + usuarioId;
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        carritoRepository.deleteAll(carritos);
        carritoCacheService.removeFromCache(cacheKey);
    }

    public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }

    // Método para agregar IDs de carritoProducto al carrito
    public void agregarIdsCarritoProductoAlCarrito(Carrito carrito, List<String> carritoProductoIds) {
        // Asignar la lista de IDs de carritoProducto al carrito
        carrito.setCarritoProductoId(carritoProductoIds);
        // Actualizar el carrito en la base de datos
        actualizarCarrito(carrito);
    }

    public void eliminarCarritoProductoDelCarrito(String usuarioId, String carritoProductoId) {
        Optional<Carrito> carritoOptional = obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            // Lógica para eliminar el carritoProducto del carrito por su ID
            carrito.getCarritoProductoId().removeIf(id -> id.equals(carritoProductoId));
            // Actualizar el carrito en la base de datos
            actualizarCarrito(carrito);
        }
    }

    // Método para crear un nuevo carrito cuando se realiza una compra
    public Carrito crearNuevoCarritoDespuesCompra(Carrito carritoAnterior) {
        // Desactivar el carrito anterior
        carritoAnterior.setActivo(false);
        actualizarCarrito(carritoAnterior);
        
        // Crear un nuevo carrito
        return crearCarrito(carritoAnterior.getUsuarioId());
    }
}
