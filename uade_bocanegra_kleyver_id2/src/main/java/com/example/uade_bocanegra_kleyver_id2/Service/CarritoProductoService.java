package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Entity.Producto;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoProductoRepository;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoRepository;
import com.example.uade_bocanegra_kleyver_id2.Request.CarritoProductoRequest;

@Service
public class CarritoProductoService {

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoService productoService; // Asumiendo que tienes un servicio ProductoService

    public CarritoProducto agregarProductoAlCarrito(String carritoId, CarritoProductoRequest productoRequest) {
        String productoId = productoRequest.getProductoId();
        int cantidad = productoRequest.getCantidad();
    
        if (!verificarStockDisponible(productoId, cantidad, carritoId)) {
            throw new IllegalStateException("No hay suficiente stock disponible para el producto solicitado");
        }
    
        Optional<Producto> productoOptional = productoService.getProductoById(productoId);
        Producto producto = productoOptional.orElseThrow(() -> new IllegalArgumentException("El producto con el ID proporcionado no existe"));
    
        double precioCarritoDelProducto = producto.getPrecio() * cantidad;
    
        CarritoProducto carritoProducto = new CarritoProducto();
        carritoProducto.setProductoId(productoId);
        carritoProducto.setCantidad(cantidad);
        carritoProducto.setPrecioCarritoDelProducto(precioCarritoDelProducto);
        carritoProducto.setCarritoId(carritoId);
    
        return carritoProductoRepository.save(carritoProducto);
    }
    
    

    public void eliminarCarritoProductoDelCarrito(String carritoProductoId) {
        // Eliminar el producto del carrito de la base de datos
        carritoProductoRepository.deleteById(carritoProductoId);
    }

    public Optional<CarritoProducto> obtenerProductoEnCarrito(String carritoProductoId) {
        // Buscar el producto en el carrito por su ID
        return carritoProductoRepository.findById(carritoProductoId);
    }

    // Método para verificar el stock disponible
    private boolean verificarStockDisponible(String productoId, int cantidadRequerida, String carritoId) {
        // Obtener la cantidad disponible en stock del producto
        int cantidadDisponible = productoService.obtenerCantidadDisponibleEnStock(productoId);
    
        // Obtener la cantidad de productos ya agregados al carrito
        int cantidadEnCarrito = obtenerCantidadProductosEnCarrito(productoId, carritoId);
    
        // Verificar si hay suficiente stock disponible
        return cantidadDisponible - cantidadEnCarrito >= cantidadRequerida;
    }


// Método para obtener la cantidad de productos del mismo tipo ya agregados al carrito
private int obtenerCantidadProductosEnCarrito(String productoId, String carritoId) {
    List<CarritoProducto> productosEnCarrito = carritoProductoRepository.findByProductoIdAndCarritoId(productoId, carritoId);
    int cantidadEnCarrito = 0;
    for (CarritoProducto cp : productosEnCarrito) {
        cantidadEnCarrito += cp.getCantidad();
    }
    return cantidadEnCarrito;
}

public List<CarritoProducto> getAllCarritoProducto() {
    return carritoProductoRepository.findAll();
}

public Optional<CarritoProducto> getCarritoProductoById(String carritoProductoId) {
    return carritoProductoRepository.findById(carritoProductoId);
}


public List<CarritoProducto> getCarritoProductoByCarritoId(String carritoId) {
    return carritoProductoRepository.findByCarritoId(carritoId);
}

public CarritoProducto modificarCantidadProductoEnCarrito(String carritoProductoId, int nuevaCantidad) {
    Optional<CarritoProducto> carritoProductoOptional = carritoProductoRepository.findById(carritoProductoId);
    if (!carritoProductoOptional.isPresent()) {
        throw new IllegalArgumentException("El producto en el carrito con el ID proporcionado no existe");
    }

    CarritoProducto carritoProducto = carritoProductoOptional.get();

    // Verificar si hay suficiente stock disponible para la nueva cantidad
    if (!verificarStockDisponible(carritoProducto.getProductoId(), nuevaCantidad, carritoProducto.getCarritoId())) {
        throw new IllegalStateException("No hay suficiente stock disponible para la cantidad solicitada");
    }

    carritoProducto.setCantidad(nuevaCantidad);

    // Actualizar el precio total del producto en el carrito
    Optional<Producto> productoOptional = productoService.getProductoById(carritoProducto.getProductoId());
    Producto producto = productoOptional.orElseThrow(() -> new IllegalArgumentException("El producto con el ID proporcionado no existe"));
    carritoProducto.setPrecioCarritoDelProducto(producto.getPrecio() * nuevaCantidad);

    return carritoProductoRepository.save(carritoProducto);
}

public Optional<CarritoProducto> obtenerCarritoProductoPorId(String carritoProductoId) {
    return carritoProductoRepository.findById(carritoProductoId);
}

public List<CarritoProducto> obtenerCarritoProductoPorCarritoId(String carritoId) {
    return carritoProductoRepository.findByCarritoId(carritoId);
}

// Método para actualizar el carrito después de eliminar un producto
public void actualizarCarritoDespuesDeEliminarProducto(CarritoProducto carritoProducto) {        
    // Encuentra todos los carritos que contienen el producto y actualiza sus listas
    List<Carrito> carritos = carritoRepository.findByCarritoProductoId(carritoProducto.getId());
    carritos.forEach(carrito -> {
        carrito.getCarritoProductoId().remove(carritoProducto.getId());
        
        // Recalcular el precio total del carrito
        double precioTotal = carrito.getCarritoProductoId().stream()
                .mapToDouble(this::obtenerPrecioProductoEnCarrito)
                .sum();
        carrito.setPrecioTotal(precioTotal);
        
        carritoRepository.save(carrito);
    });
}


        // Método para obtener el precio de un producto en el carrito
        private double obtenerPrecioProductoEnCarrito(String carritoProductoId) {
            Optional<CarritoProducto> carritoProductoOptional = carritoProductoRepository.findById(carritoProductoId);
            if (carritoProductoOptional.isPresent()) {
                return carritoProductoOptional.get().getPrecioCarritoDelProducto();
            }
            return 0; // Si no se encuentra el producto, retornamos 0
        }


}
