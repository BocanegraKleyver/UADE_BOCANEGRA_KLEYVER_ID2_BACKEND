package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;

@Repository
public interface CarritoProductoRepository extends MongoRepository<CarritoProducto, String> {

    List<CarritoProducto> findByCarritoId(String carritoId);

    List<CarritoProducto> findByProductoId(String productoId);

    // Otros métodos según sea necesario
}
