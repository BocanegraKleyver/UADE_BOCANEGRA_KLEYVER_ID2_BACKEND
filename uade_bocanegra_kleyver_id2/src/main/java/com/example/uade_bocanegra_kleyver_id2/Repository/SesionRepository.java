package com.example.uade_bocanegra_kleyver_id2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;

public interface SesionRepository extends MongoRepository<Sesion, String> {
    // Aquí puedes agregar métodos personalizados si los necesitas en el futuro
    // Por ahora, los métodos heredados de MongoRepository son suficientes para operaciones CRUD
}
