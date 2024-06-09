package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoProductoRepository;

@Service
public class CarritoProductoService {

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    public CarritoProducto agregarProductoAlCarrito(CarritoProducto carritoProducto) {
        // Generar un ID único para el carritoProducto
        String carritoProductoId = UUID.randomUUID().toString();
        carritoProducto.setId(carritoProductoId);
        
        // Guardar el producto en el carrito en la base de datos
        CarritoProducto savedCarritoProducto = carritoProductoRepository.save(carritoProducto);
        return savedCarritoProducto;
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
