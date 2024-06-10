package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.uade_bocanegra_kleyver_id2.Entity.CarritoProducto;

@Repository
public interface CarritoProductoRepository extends MongoRepository<CarritoProducto, String> {
    
    List<CarritoProducto> findByCarritoId(String carritoId);

    List<CarritoProducto> findByProductoId(String productoId);
    
    List<CarritoProducto> findByCarritoIdAndProductoId(String carritoId, String productoId);

      // Corrección aquí, cambia el nombre del método para que coincida con lo que necesitas
      List<CarritoProducto> findByProductoIdAndCarritoId(String productoId, String carritoId);
      
}
