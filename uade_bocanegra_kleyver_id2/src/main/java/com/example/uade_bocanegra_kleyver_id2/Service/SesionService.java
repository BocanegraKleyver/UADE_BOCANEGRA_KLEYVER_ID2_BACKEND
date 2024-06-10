package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional; // Importa Optional de java.util

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;
import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.SesionRepository;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private CacheService<Sesion> sesionCacheService;

    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Sesion getSesionById(String id) {
        return sesionRepository.findById(id).orElse(null);
    }

    public Sesion getSesionActivaByUsuarioId(String usuarioId) {
        Optional<Sesion> sesionOptional = sesionRepository.findFirstByUsuarioIdAndFechaFinIsNullOrderByFechaInicioDesc(usuarioId);
        return sesionOptional.orElse(null); // Si está presente, devuelve la sesión, de lo contrario, devuelve null
    }

    public Sesion iniciarSesion(Usuario usuario) {
        Sesion sesion = new Sesion(usuario.getId(), new Date(), null); // Usar ID de usuario y fecha actual para iniciar sesión
        Sesion savedSesion = sesionRepository.save(sesion);
        sesionCacheService.addToCache(savedSesion.getId(), savedSesion);
        return savedSesion;
    }

    public Sesion cerrarSesion(String id) {
        Optional<Sesion> existingSesionOptional = sesionRepository.findById(id);
        if (existingSesionOptional.isPresent()) {
            Sesion existingSesion = existingSesionOptional.get();
            existingSesion.setFechaFin(new Date());
            sesionRepository.save(existingSesion);
            sesionCacheService.removeFromCache(id);
            return existingSesion;
        }
        return null; // Retornar null si no se encuentra la sesión
    }
    

    public void deleteSesion(String id) {
        sesionRepository.deleteById(id);
        sesionCacheService.removeFromCache(id);
    }
}
