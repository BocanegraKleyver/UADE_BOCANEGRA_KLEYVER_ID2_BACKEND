package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoProductoRepository;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CacheService<Carrito> carritoCacheService;
     
    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

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

    
    // Método para agregar IDs de carritoProducto al carrito y actualizar el precio total
    public void agregarIdsCarritoProductoAlCarrito(String carritoId, List<String> idsCarritoProducto) {
        Optional<Carrito> carritoOptional = carritoRepository.findById(carritoId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            Set<String> carritoProductoIds = new HashSet<>(carrito.getCarritoProductoId());
            carritoProductoIds.addAll(idsCarritoProducto);
            carrito.setCarritoProductoId(List.copyOf(carritoProductoIds)); // Convertir el Set a List
            actualizarPrecioTotal(carrito);
            carritoRepository.save(carrito);
        } else {
            throw new IllegalStateException("No se encontró el carrito con el ID proporcionado.");
        }
    }

     // Método para actualizar el precio total del carrito
    private void actualizarPrecioTotal(Carrito carrito) {
        double precioTotal = 0.0;
        for (String carritoProductoId : carrito.getCarritoProductoId()) {
            Optional<CarritoProducto> carritoProductoOpt = carritoProductoRepository.findById(carritoProductoId);
            if (carritoProductoOpt.isPresent()) {
                CarritoProducto carritoProducto = carritoProductoOpt.get();
                precioTotal += carritoProducto.getPrecioCarritoDelProducto();
            }
        }
        carrito.setPrecioTotal(precioTotal);
    }


    // Método para eliminar un producto del carrito y actualizar el precio total
    public void eliminarCarritoProductoDelCarrito(String usuarioId, String carritoProductoId) {
        Optional<Carrito> carritoOptional = obtenerCarritoPorUsuarioId(usuarioId);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            carrito.getCarritoProductoId().removeIf(id -> id.equals(carritoProductoId));
            actualizarPrecioTotal(carrito);
            carritoRepository.save(carrito);
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
