package com.example.uade_bocanegra_kleyver_id2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;

@Repository
public interface CarritoProductoRepository extends MongoRepository<CarritoProducto, String> {
    // Aquí puedes agregar métodos personalizados según tus necesidades
}
