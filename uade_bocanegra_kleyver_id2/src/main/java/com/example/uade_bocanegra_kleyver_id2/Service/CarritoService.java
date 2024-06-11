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

    @Autowired
    private CarritoProductoService carritoProductoService;

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


    public void eliminarCarritoProductoDelCarrito(String usuarioId, String carritoProductoId) {
        List<Carrito> carritos = findByUsuarioId(usuarioId);
        for (Carrito carrito : carritos) {
            if (carrito.getCarritoProductoId().contains(carritoProductoId)) {
                carrito.getCarritoProductoId().remove(carritoProductoId);
                carritoRepository.save(carrito);
                break; // Si solo puede haber un carrito por usuario, puedes salir del bucle aquí
            }
        }
    }

    public List<Carrito> findByUsuarioId(String usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
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
    

    public void actualizarCarritoDespuesDeEliminarProducto(String carritoProductoId) {        
        // Encuentra todos los carritos que contienen el producto y actualiza sus listas
        List<Carrito> carritos = carritoRepository.findByCarritoProductoId(carritoProductoId);
        carritos.forEach(carrito -> {
            // Eliminar el producto del carrito
            carrito.getCarritoProductoId().remove(carritoProductoId);
            
            // Recalcular el precio total del carrito
            double precioTotal = carrito.getCarritoProductoId().stream()
                .mapToDouble(id -> {
                    double precio = obtenerPrecioProductoEnCarrito(id);
                    System.out.println("Precio del producto con ID " + id + ": " + precio);
                    return precio;
                })
                .sum();
            System.out.println("Nuevo precio total del carrito: " + precioTotal);
    
            carrito.setPrecioTotal(precioTotal);
    
            // Guardar el carrito actualizado
            carritoRepository.save(carrito);
        });
    }

public void actualizarCantidadProductoEnCarrito(String carritoId, String carritoProductoId, int nuevaCantidad) {
    Optional<Carrito> carritoOptional = carritoRepository.findById(carritoId);
    if (carritoOptional.isPresent()) {
        Carrito carrito = carritoOptional.get();
        Optional<CarritoProducto> carritoProductoOpt = carritoProductoRepository.findById(carritoProductoId);
        if (carritoProductoOpt.isPresent()) {
            CarritoProducto carritoProducto = carritoProductoOpt.get();
            carritoProducto.setCantidad(nuevaCantidad);
            double nuevoPrecio = carritoProductoService.calcularNuevoPrecio(carritoProductoId, nuevaCantidad);
            carritoProducto.setPrecioCarritoDelProducto(nuevoPrecio);
            carritoProductoRepository.save(carritoProducto);
            actualizarPrecioTotal(carrito);
            carritoRepository.save(carrito);
        }
    }
}


// Método para obtener el precio de un producto en el carrito
private double obtenerPrecioProductoEnCarrito(String carritoProductoId) {
    Optional<CarritoProducto> carritoProductoOptional = carritoProductoRepository.findById(carritoProductoId);
    if (carritoProductoOptional.isPresent()) {
        double precio = carritoProductoOptional.get().getPrecioCarritoDelProducto();
        System.out.println("Precio del producto con ID " + carritoProductoId + ": " + precio);
        return precio;
    }
    System.out.println("No se encontró el producto con ID " + carritoProductoId);
    return 0; // Si no se encuentra el producto, retornamos 0
}


public void actualizarCarritoDespuesDeModificarCantidadProducto(String carritoId) {
    Optional<Carrito> carritoOptional = carritoRepository.findById(carritoId);
    if (carritoOptional.isPresent()) {
        Carrito carrito = carritoOptional.get();

        // Recalcular el precio total del carrito
        double precioTotal = carrito.getCarritoProductoId().stream()
            .mapToDouble(this::obtenerPrecioProductoEnCarrito)
            .sum();
        
        carrito.setPrecioTotal(precioTotal);

        // Guardar el carrito actualizado
        carritoRepository.save(carrito);
        
        // Log para verificar el precio total actualizado
        System.out.println("Precio total actualizado del carrito " + carritoId + ": " + precioTotal);
    }
}

public Carrito obtenerCarritoPorIdcarrito(String carritoId) {
    Optional<Carrito> carritoOptional = carritoRepository.findById(carritoId);
    return carritoOptional.orElse(null); // Devuelve el carrito si está presente, o null si no se encuentra
}
}
