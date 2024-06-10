package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Producto;

public interface ProductoRepository extends MongoRepository<Producto, String> {

    List<Producto> findByNombre(String nombre);

    List<Producto> findByPrecioLessThan(double precio);

    // Otros métodos según sea necesario
}