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
import com.example.uade_bocanegra_kleyver_id2.Repository.ProductoRepository;
import com.example.uade_bocanegra_kleyver_id2.Request.CarritoProductoRequest;

@Service
public class CarritoProductoService {

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoService productoService; // Asumiendo que tienes un servicio ProductoService

        @Autowired
    private ProductoRepository productoRepository;

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
    Optional<CarritoProducto> carritoProductoOpt = carritoProductoRepository.findById(carritoProductoId);
    if (carritoProductoOpt.isPresent()) {
        CarritoProducto carritoProducto = carritoProductoOpt.get();
        carritoProducto.setCantidad(nuevaCantidad);
        carritoProducto.setPrecioCarritoDelProducto(calcularNuevoPrecio(carritoProducto.getProductoId(), nuevaCantidad));
        return carritoProductoRepository.save(carritoProducto);
    } else {
        throw new IllegalStateException("CarritoProducto not found");
    }
}
// Implementación del método para calcular el nuevo precio basado en la cantidad
public double calcularNuevoPrecio(String productoId, int cantidad) { // Cambiado a public
    // Aquí puedes agregar la lógica para calcular el nuevo precio.
    // Por ejemplo, podrías obtener el precio del producto desde la base de datos y multiplicarlo por la cantidad.
    Optional<Producto> productoOpt = productoRepository.findById(productoId);
    if (productoOpt.isPresent()) {
        Producto producto = productoOpt.get();
        return producto.getPrecio() * cantidad;
    }
    throw new IllegalStateException("Producto not found");
}

public Optional<CarritoProducto> obtenerCarritoProductoPorId(String carritoProductoId) {
    return carritoProductoRepository.findById(carritoProductoId);
}

public List<CarritoProducto> obtenerCarritoProductoPorCarritoId(String carritoId) {
    return carritoProductoRepository.findByCarritoId(carritoId);
}

public void actualizarCarritoDespuesDeEliminarProducto(String carritoProductoId) {        
    // Encuentra todos los carritos que contienen el producto y actualiza sus listas
    List<Carrito> carritos = carritoRepository.findByCarritoProductoId(carritoProductoId);
    carritos.forEach(carrito -> {
        // Eliminar el producto del carrito
        System.out.println("Antes de eliminar: " + carrito.getCarritoProductoId());
        carrito.getCarritoProductoId().removeIf(id -> id.equals(carritoProductoId));
        System.out.println("Después de eliminar: " + carrito.getCarritoProductoId());
        
        // Recalcular el precio total del carrito
        double precioTotal = carrito.getCarritoProductoId().stream()
            .mapToDouble(this::obtenerPrecioProductoEnCarrito)
            .sum();
        
        System.out.println("Nuevo precio total del carrito: " + precioTotal);
        
        carrito.setPrecioTotal(precioTotal);

        // Guardar el carrito actualizado
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

}
