package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Pago;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CacheService<Pago> pagoCacheService;

    // Obtener todos los pagos
    public List<Pago> getAllPagos() {
        return pagoRepository.findAll();
    }

    // Obtener un pago por su ID
    public Optional<Pago> getPagoById(String id) {
        // Primero, intentamos obtener el pago de la caché
        Pago pago = pagoCacheService.getFromCache(id);

        if (pago != null) {
            return Optional.of(pago);
        } else {
            // Si no está en caché, consultamos en la base de datos
            Optional<Pago> pagoOpt = pagoRepository.findById(id);
            pagoOpt.ifPresent(p -> pagoCacheService.addToCache(p.getId(), p));
            return pagoOpt;
        }
    }

    // Guardar un nuevo pago
    public Pago savePago(Pago pago) {
        // Guardar el pago en la base de datos
        Pago savedPago = pagoRepository.save(pago);
        
        // Agregar el pago a la caché
        pagoCacheService.addToCache(savedPago.getId(), savedPago);
        
        return savedPago;
    }

    // Eliminar un pago por su ID
    public void deletePago(String id) {
        // Eliminar el pago de la base de datos
        pagoRepository.deleteById(id);
        
        // Eliminar el pago de la caché
        pagoCacheService.removeFromCache(id);
    }

    // Obtener pagos por ID de usuario
    public List<Pago> getPagosByUsuarioId(String usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }

    // Obtener pagos por ID de pedido
    public List<Pago> getPagosByPedidoId(String pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }

    // Actualizar método de pago
    public Pago actualizarMetodoPago(String pagoId, String metodoPago) {
        Optional<Pago> pagoOptional = pagoRepository.findById(pagoId);
        if (pagoOptional.isPresent()) {
            Pago pago = pagoOptional.get();
            pago.setMetodoPago(metodoPago);
            return pagoRepository.save(pago);
        } else {
            throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
        }
    }

    // Actualizar método de envío
    public Pago actualizarMetodoEnvio(String pagoId, String metodoEnvio) {
        Optional<Pago> pagoOptional = pagoRepository.findById(pagoId);
        if (pagoOptional.isPresent()) {
            Pago pago = pagoOptional.get();
            pago.setMetodoEnvio(metodoEnvio);
            return pagoRepository.save(pago);
        } else {
            throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
        }
    }

        // Actualizar notas del cliente
        public Pago actualizarNotasCliente(String pagoId, String notasCliente) {
            Optional<Pago> pagoOptional = pagoRepository.findById(pagoId);
            if (pagoOptional.isPresent()) {
                Pago pago = pagoOptional.get();
                pago.setNotasCliente(notasCliente);
                return pagoRepository.save(pago);
            } else {
                throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
            }
        }
}
