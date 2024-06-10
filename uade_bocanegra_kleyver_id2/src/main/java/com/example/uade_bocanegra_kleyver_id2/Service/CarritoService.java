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
        
        // Agregar console.log para verificar
        System.out.println("Carrito creado y guardado: " + savedCarrito.getId() + " para usuario: " + usuarioId);
        
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
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        if (!carritos.isEmpty()) {
            Carrito carrito = carritos.get(0); // Tomamos el primer carrito de la lista
            carritoCacheService.addToCache("carrito:" + usuarioId, carrito);
            return carritos.stream().filter(Carrito::isActivo).findFirst();
        }
        return Optional.empty();
    }

    public void marcarCarritoComoCerrado(String usuarioId) {
        Optional<Carrito> carritoOptional = obtenerCarritoPorUsuarioId(usuarioId);
        carritoOptional.ifPresent(carrito -> {
            carrito.setActivo(false); // Marcar el carrito como inactivo
            carrito.setFechaModificacion(new Date()); // Actualizar la fecha de modificación
            carritoRepository.save(carrito); // Guardar los cambios en la base de datos
            
            // Agregar console log para mostrar el cambio de estado
            System.out.println("ID del carrito: " + carrito.getId() + " cambió su estado a inactivo.");
        });
    }

    public void eliminarCarrito(String usuarioId) {
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        for (Carrito carrito : carritos) {
            carrito.setActivo(false);
            carritoRepository.save(carrito);
        }
    }


    public List<Carrito> getAllCarritos() {
        return carritoRepository.findAll();
    }

    
    // Método para agregar IDs de carritoProducto al carrito
    public void agregarIdsCarritoProductoAlCarrito(String carritoId, List<String> idsCarritoProducto) {
        // Obtener el carrito por su ID
        Optional<Carrito> carritoOptional = carritoRepository.findById(carritoId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            
            // Obtener la lista actual de IDs de productos del carrito y agregar los nuevos IDs
            List<String> carritoProductoIds = carrito.getCarritoProductoId();
            carritoProductoIds.addAll(idsCarritoProducto);
            
            // Actualizar el carrito con los nuevos IDs de productos
            carrito.setCarritoProductoId(carritoProductoIds);
            carritoRepository.save(carrito);
        } else {
            throw new IllegalStateException("No se encontró el carrito con el ID proporcionado.");
        }
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


    public Carrito modificarEstadoCarrito(String usuarioId, String estado) {
        List<Carrito> carritos = carritoRepository.findByUsuarioId(usuarioId);
        for (Carrito carrito : carritos) {
            carrito.setActivo("activo".equals(estado));
            carrito.setFechaModificacion(new Date());
            carritoRepository.save(carrito);
        }
        return carritos.isEmpty() ? null : carritos.get(0);
    }
    

}
