package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.UsuarioActividad;

public interface UsuarioActividadRepository extends MongoRepository<UsuarioActividad, String> {

    
    List<UsuarioActividad> findBySesionId(String sesionId);


}
