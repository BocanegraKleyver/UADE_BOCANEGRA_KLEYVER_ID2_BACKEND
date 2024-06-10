package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;
import com.example.uade_bocanegra_kleyver_id2.Repository.CarritoProductoRepository;

@Service
public class CarritoProductoService {

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    public List<CarritoProducto> agregarProductosAlCarrito(Carrito carrito, List<CarritoProducto> productos) {
        for (CarritoProducto producto : productos) {
            producto.setCarritoId(carrito.getId());
        }
        // Guardar los productos en la base de datos
        List<CarritoProducto> savedProductos = carritoProductoRepository.saveAll(productos);
        return savedProductos;
    }

    public void eliminarProductoDelCarrito(String carritoProductoId) {
        // Eliminar el producto del carrito de la base de datos
        carritoProductoRepository.deleteById(carritoProductoId);
    }

    public Optional<CarritoProducto> obtenerProductoEnCarrito(String carritoProductoId) {
        // Buscar el producto en el carrito por su ID
        return carritoProductoRepository.findById(carritoProductoId);
    }

    // Otros métodos según sea necesario
}
