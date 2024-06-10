package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Producto;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CacheService<Producto> productoCacheService;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(String id) {
        // Primero, intentamos obtener el producto de la caché
        Producto producto = productoCacheService.getFromCache(id);

        if (producto != null) {
            return Optional.of(producto);
        } else {
            // Si no está en caché, consultamos en la base de datos
            Optional<Producto> productoOpt = productoRepository.findById(id);
            productoOpt.ifPresent(p -> productoCacheService.addToCache(p.getId(), p));
            return productoOpt;
        }
    }

    public Producto saveProducto(Producto producto) {
        // Guardar el producto en la base de datos
        Producto savedProducto = productoRepository.save(producto);
        
        // Agregar el producto a la caché
        productoCacheService.addToCache(savedProducto.getId(), savedProducto);
        
        return savedProducto;
    }

    public void deleteProducto(String id) {
        // Eliminar el producto de la base de datos
        productoRepository.deleteById(id);
        
        // Eliminar el producto de la caché
        productoCacheService.removeFromCache(id);
    }

    // Método para obtener la cantidad disponible en stock de un producto
    public int obtenerCantidadDisponibleEnStock(String productoId) {
        Optional<Producto> optionalProducto = getProductoById(productoId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            return producto.getCantidad(); // Suponiendo que la cantidad está almacenada en el producto
        }
        return 0; // Retornamos 0 si el producto no se encuentra o no tiene cantidad definida
    }

    // Método para verificar si hay suficiente stock disponible
    public boolean verificarStockDisponible(String productoId, int cantidadRequerida) {
        Optional<Producto> optionalProducto = getProductoById(productoId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            int cantidadDisponible = producto.getCantidad();
            return cantidadDisponible >= cantidadRequerida;
        }
        return false; // Si el producto no se encuentra, retornamos falso
    }
}
