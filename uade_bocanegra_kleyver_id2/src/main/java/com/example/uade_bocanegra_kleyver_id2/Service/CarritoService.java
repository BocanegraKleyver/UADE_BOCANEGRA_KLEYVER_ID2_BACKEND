package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
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
        Carrito carrito = carritoCacheService.getFromCache(cacheKey);
        if (carrito != null) {
            return Optional.of(carrito);
        } else {
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
        Carrito savedCarrito = carritoRepository.save(carrito);
        carritoCacheService.addToCache("carrito:" + usuarioId, savedCarrito);
        return savedCarrito;
    }

    public Carrito actualizarCarrito(Carrito carrito) {
        carrito.setFechaModificacion(new Date());
        Carrito updatedCarrito = carritoRepository.save(carrito);
        carritoCacheService.addToCache("carrito:" + carrito.getUsuarioId(), updatedCarrito);
        return updatedCarrito;
    }

    public void eliminarProductoDelCarrito(String usuarioId, String productoId) {
        Optional<Carrito> carritoOptional = obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            carrito.getCarritoProductos().removeIf(producto -> producto.getProductoId().equals(productoId));
            Carrito updatedCarrito = carritoRepository.save(carrito);
            carritoCacheService.addToCache("carrito:" + usuarioId, updatedCarrito);
        }
    }

    public void marcarCarritoComoCerrado(String usuarioId) {
        Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            carrito.setEstado("cerrado");
            carritoRepository.save(carrito);
        }
    }

    public void eliminarCarrito(String usuarioId) {
        String cacheKey = "carrito:" + usuarioId;
        Optional<Carrito> carritoOptional = carritoRepository.findByUsuarioId(usuarioId);
        carritoOptional.ifPresent(carrito -> carritoRepository.delete(carrito));
        carritoCacheService.removeFromCache(cacheKey);
    }

    public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }

    // Método para agregar productos al carrito
    public void agregarProductosAlCarrito(Carrito carrito, List<CarritoProducto> productos) {
        carrito.getCarritoProductos().addAll(productos);
        actualizarCarrito(carrito);
    }
}
