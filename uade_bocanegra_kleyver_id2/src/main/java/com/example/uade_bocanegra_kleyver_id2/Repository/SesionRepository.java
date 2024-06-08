package com.example.uade_bocanegra_kleyver_id2.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;

public interface SesionRepository extends MongoRepository<Sesion, ObjectId> {
    // No necesitas definir un método findBySesion ya que findById está disponible por defecto
}
