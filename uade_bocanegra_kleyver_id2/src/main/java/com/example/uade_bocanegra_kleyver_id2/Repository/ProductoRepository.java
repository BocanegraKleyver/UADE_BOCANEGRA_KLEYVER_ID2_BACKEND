package com.example.uade_bocanegra_kleyver_id2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Producto;

public interface ProductoRepository extends MongoRepository<Producto, String> {
}