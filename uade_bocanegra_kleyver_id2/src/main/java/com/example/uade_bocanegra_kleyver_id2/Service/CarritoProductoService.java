package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Redis.CarritoCacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoProductoRepository;

@Service
public class CarritoProductoService {

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private CarritoCacheService carritoCacheService;

    @Autowired
    private ProductoService productoService; // Importa el servicio de productos para obtener información sobre el stock

    public CarritoProducto agregarProductoAlCarrito(CarritoProducto carritoProducto) {
        // Verificar si la cantidad solicitada está disponible en el stock
        if (!verificarStockDisponible(carritoProducto.getProductoId(), carritoProducto.getCantidad())) {
            throw new RuntimeException("No hay suficiente stock disponible para este producto.");
        }

        // Generar un ID único para el carritoProducto
        String carritoProductoId = UUID.randomUUID().toString();
        carritoProducto.setId(carritoProductoId);
        
        // Guardar el producto en el carrito en la base de datos
        CarritoProducto savedCarritoProducto = carritoProductoRepository.save(carritoProducto);
        return savedCarritoProducto;
    }

    private boolean verificarStockDisponible(String productoId, int cantidad) {
        // Obtener la cantidad disponible en stock del producto
        int cantidadDisponible = productoService.obtenerCantidadDisponibleEnStock(productoId);
        // Verificar si la cantidad solicitada es menor o igual que la disponible en stock
        return cantidadDisponible >= cantidad;
    }
    

    public void eliminarProductoDelCarrito(String carritoProductoId) {
        // Eliminar el producto del carrito de la base de datos
        carritoProductoRepository.deleteById(carritoProductoId);
    }

    public Optional<CarritoProducto> obtenerProductoEnCarrito(String carritoProductoId) {
        // Buscar el producto en el carrito por su ID
        return carritoProductoRepository.findById(carritoProductoId);
    }
}
