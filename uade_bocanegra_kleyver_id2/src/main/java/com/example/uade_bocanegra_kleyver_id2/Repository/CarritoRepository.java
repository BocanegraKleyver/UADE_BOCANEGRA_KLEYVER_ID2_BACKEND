package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;

public interface CarritoRepository extends MongoRepository<Carrito, String> {
    Optional<Carrito> findByUsuarioId(String usuarioId);
}



