package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;

public interface SesionRepository extends MongoRepository<Sesion, String> {

    Optional<Sesion> findFirstByUsuarioIdAndFechaFinIsNull(String usuarioId);
    List<Sesion> findByUsuarioIdAndFechaFinIsNull(String usuarioId);
    
    

}