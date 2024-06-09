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

    
    public Optional<Carrito> obtenerCarritoPorUsuarioId(String usuarioId) {
        String cacheKey = "carrito:" + usuarioId;

        // Primero, intentamos obtener el carrito de la caché
        Carrito carrito = carritoCacheService.getFromCache(cacheKey);

        if (carrito != null) {
            return Optional.of(carrito);
        } else {
            // Si no está en caché, consultamos en la base de datos
            Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioId(usuarioId);
            carritoOptional.ifPresent(value -> carritoCacheService.addToCache(cacheKey, value));
            return carritoOptional;
        }
    }

    public Carrito crearCarrito(String usuarioId) {
        Carrito carrito = new Carrito();
        carrito.setUsuarioId(usuarioId);
        carrito.setEstado("activo");
        carrito.setFechaCreacion(new Date());
        carrito.setFechaModificacion(new Date());

        // Guardar el carrito en la base de datos
        Carrito savedCarrito = carritoRepository.save(carrito);

        // Agregar el carrito a la caché
        carritoCacheService.addToCache("carrito:" + usuarioId, savedCarrito);

        return savedCarrito;
    }

    public Carrito actualizarCarrito(Carrito carrito) {
        carrito.setFechaModificacion(new Date());

        // Guardar el carrito en la base de datos
        Carrito updatedCarrito = carritoRepository.save(carrito);

        // Agregar el carrito a la caché
        carritoCacheService.addToCache("carrito:" + carrito.getUsuarioId(), updatedCarrito);

        return updatedCarrito;
    }

    public void eliminarProductoDelCarrito(String usuarioId, String productoId) {
        Optional<Carrito> carritoOptional = obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            carrito.getProductos().removeIf(producto -> producto.getProductoId().equals(productoId));

            // Guardar el carrito actualizado en la base de datos
            Carrito updatedCarrito = carritoRepository.save(carrito);

            // Actualizar el carrito en la caché
            carritoCacheService.addToCache("carrito:" + usuarioId, updatedCarrito);
        }
    }


    public void marcarCarritoComoCerrado(String usuarioId) {
        Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            carrito.setEstado("cerrado"); // Aquí pasamos directamente el string "cerrado"
            carritoRepository.save(carrito);
        }
    }

    public void eliminarCarrito(String usuarioId) {
        String cacheKey = "carrito:" + usuarioId;

        // Eliminar el carrito de la base de datos
        Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioId(usuarioId);
        carritoOptional.ifPresent(carrito -> carritoRepository.delete(carrito));

        // Eliminar el carrito de la caché
        carritoCacheService.removeFromCache(cacheKey);
    }

        public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }
}