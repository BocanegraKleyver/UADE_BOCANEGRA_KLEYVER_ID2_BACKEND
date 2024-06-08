package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Producto;
import com.example.uade_bocanegra_kleyver_id2.Redis.ProductoCacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoCacheService productoCacheService;

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
            return productoRepository.findById(id);
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
}