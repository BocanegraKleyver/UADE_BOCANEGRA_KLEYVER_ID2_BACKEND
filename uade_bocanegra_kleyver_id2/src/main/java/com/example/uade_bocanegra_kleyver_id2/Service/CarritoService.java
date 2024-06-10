package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.ArrayList;
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

    // Método para agregar productos al carrito
    public List<CarritoProducto> agregarProductosAlCarrito(Carrito carrito, List<String> productosIds) {
        List<CarritoProducto> savedProductos = new ArrayList<>();
        // Lógica para crear o cargar los productos y asociarlos al carrito
        for (String productoId : productosIds) {
            CarritoProducto carritoProducto = new CarritoProducto();
            // Asignar el carrito y el ID del producto al carritoProducto
            carritoProducto.setCarritoId(carrito.getId());
            carritoProducto.setProductoId(productoId); // Asignar el ID del producto
            // Puedes agregar más propiedades al carritoProducto si es necesario
            savedProductos.add(carritoProducto);
        }
        // Guardar los productos en la base de datos si es necesario
        // carritoProductoRepository.saveAll(savedProductos);
        return savedProductos;
    }

    public void eliminarProductoDelCarrito(String usuarioId, String productoId) {
        Optional<Carrito> carritoOptional = obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            // Lógica para eliminar el producto del carrito
            carrito.getCarritoProductos().removeIf(producto -> producto.getProductoId().equals(productoId));
            actualizarCarrito(carrito);
        }

}
}