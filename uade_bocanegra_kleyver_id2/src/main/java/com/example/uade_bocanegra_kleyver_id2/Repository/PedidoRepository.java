package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String> {

    List<Pedido> findByUsuarioId(String usuarioId);
    List<Pedido> findByCarritoId(String carritoId); 
}
