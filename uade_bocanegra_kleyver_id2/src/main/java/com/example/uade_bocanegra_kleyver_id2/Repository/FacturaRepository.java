package com.example.uade_bocanegra_kleyver_id2.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Factura;

@Repository
public interface FacturaRepository extends MongoRepository<Factura, String> {

    List<Factura> findByPagoId(String pagoId);// Método para buscar pagos por ID de pago
    List<Factura> findByUsuarioId(String usuarioId); // Método para buscar pagos por ID de usuario
    List<Factura> findByPedidoId(String pedidoId); // Método para buscar pagos por ID de pedido
}
