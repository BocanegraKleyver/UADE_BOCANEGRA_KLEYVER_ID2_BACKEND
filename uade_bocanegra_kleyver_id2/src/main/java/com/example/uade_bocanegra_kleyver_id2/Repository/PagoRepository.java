package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Pago;

public interface PagoRepository extends MongoRepository<Pago, String> {

    List<Pago> findByPedidoId(String pedidoId);
    List<Pago> findByUsuarioId(String usuarioId); // Método para buscar pagos por ID de usuario
    // Puedes agregar más métodos de búsqueda según tus necesidades
}
