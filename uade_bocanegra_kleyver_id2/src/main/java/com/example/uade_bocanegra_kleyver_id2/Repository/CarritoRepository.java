package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;

public interface CarritoRepository extends MongoRepository<Carrito, String> {

    List<Carrito> findByUsuarioId(String usuarioId);

    List<Carrito> findByActivo(boolean activo);

    List<Carrito> findByFechaCreacionBetween(Date startDate, Date endDate);
    
    List<Carrito> findByCarritoProductoId(String carritoProductoId); // Método para buscar por carritoProductoId
    
    
}
