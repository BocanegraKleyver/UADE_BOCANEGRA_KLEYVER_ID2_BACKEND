package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
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
        Producto producto = productoCacheService.getFromCache(id);
        if (producto != null) {
            return Optional.of(producto);
        } else {
            Optional<Producto> productoOpt = productoRepository.findById(id);
            productoOpt.ifPresent(p -> productoCacheService.addToCache(p.getId(), p));
            return productoOpt;
        }
    }

    public Producto saveProducto(Producto producto) {
        producto.setFechaModificacion(new Date()); // Actualiza la fecha de modificación al guardar
        if (producto.getId() != null) { // Si es una actualización
            actualizarPrecioYDescuento(producto);
        }
        Producto savedProducto = productoRepository.save(producto);
        productoCacheService.addToCache(savedProducto.getId(), savedProducto);
        return savedProducto;
    }

    private void actualizarPrecioYDescuento(Producto producto) {
        Optional<Producto> optionalProducto = productoRepository.findById(producto.getId());
        optionalProducto.ifPresent(p -> {
            double precioAnterior = p.getPrecio();
            if (producto.getPrecio() != precioAnterior) {
                producto.setPrecioAnterior(precioAnterior);
            } else if (producto.getDescuento() > 0) {
                producto.setPrecioAnterior(precioAnterior);
                double precioConDescuento = precioAnterior * (1 - (producto.getDescuento() / 100));
                producto.setPrecio(precioConDescuento);
            }
        });
    }


    public void deleteProducto(String id) {
        productoRepository.deleteById(id);
        productoCacheService.removeFromCache(id);
    }

    public void actualizarFechaModificacion(String productoId) {
        Optional<Producto> optionalProducto = productoRepository.findById(productoId);
        optionalProducto.ifPresent(producto -> {
            producto.setFechaModificacion(new Date());
            productoRepository.save(producto);
        });
    }

    public int obtenerCantidadDisponibleEnStock(String productoId) {
        Optional<Producto> optionalProducto = getProductoById(productoId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            return producto.getCantidad();
        }
        return 0;
    }

    public boolean verificarStockDisponible(String productoId, int cantidadRequerida) {
        Optional<Producto> optionalProducto = getProductoById(productoId);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            int cantidadDisponible = producto.getCantidad();
            return cantidadDisponible >= cantidadRequerida;
        }
        return false;
    }
}