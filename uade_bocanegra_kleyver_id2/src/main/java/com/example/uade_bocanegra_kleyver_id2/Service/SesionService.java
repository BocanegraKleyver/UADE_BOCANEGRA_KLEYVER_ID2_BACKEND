package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;
import com.example.uade_bocanegra_kleyver_id2.Redis.RedisCacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.SesionRepository;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Sesion getSesionById(String id) {
        Sesion sesion = (Sesion) redisCacheService.getFromCache(id);
        if (sesion == null) {
            sesion = sesionRepository.findById(id).orElse(null);
            if (sesion != null) {
                redisCacheService.addToCache(id, sesion);
            }
        }
        return sesion;
    }

    public Sesion iniciarSesion(Sesion sesion) {
        sesion.setFechaInicio(new Date()); // Establecer la fecha de inicio al iniciar sesión
        Sesion savedSesion = sesionRepository.save(sesion);
        redisCacheService.addToCache(sesion.getId(), sesion);
        return savedSesion;
    }

    public Sesion cerrarSesion(String id) {
        Sesion existingSesion = sesionRepository.findById(id).orElse(null);
        if (existingSesion != null) {
            existingSesion.setFechaFin(new Date()); // Establecer la fecha de fin al cerrar sesión
            sesionRepository.save(existingSesion);
            redisCacheService.addToCache(id, existingSesion);
            return existingSesion;
        } else {
            return null;
        }
    }

    public void deleteSesion(String id) {
        sesionRepository.deleteById(id);
        redisCacheService.removeFromCache(id);
    }
}
