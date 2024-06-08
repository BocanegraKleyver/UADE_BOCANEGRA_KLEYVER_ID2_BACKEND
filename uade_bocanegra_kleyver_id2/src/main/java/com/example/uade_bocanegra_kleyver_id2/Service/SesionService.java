package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;
import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Redis.SesionCacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.SesionRepository;

@Service
public class SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private SesionCacheService sesionCacheService;

    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Sesion getSesionById(String id) {
        return sesionRepository.findById(id).orElse(null);
    }

    public Sesion iniciarSesion(Usuario usuario) {
        System.out.println("Iniciando sesión para el usuario: " + usuario.getUsuario());
        
        // Crear una nueva sesión
        Sesion sesion = new Sesion();
        
        // Obtener el ID del usuario como String
        String usuarioId = usuario.getId();
        System.out.println("ID del usuario: " + usuarioId);
        
        // Setear el ID del usuario en la sesión
        sesion.setUsuarioId(usuarioId);
        
        // Setear la fecha de inicio
        sesion.setFechaInicio(new Date());
        
        // Guardar la sesión
        Sesion savedSesion = sesionRepository.save(sesion);
        System.out.println("Sesión iniciada y guardada correctamente: " + savedSesion.getId());
        
        // Agregar la sesión a la caché
        sesionCacheService.addToCache(savedSesion.getId(), savedSesion);
    
        return savedSesion;
    }

    public Sesion cerrarSesion(String id) {
        Sesion existingSesion = sesionRepository.findById(id).orElse(null);
        if (existingSesion != null) {
            existingSesion.setFechaFin(new Date());
            sesionRepository.save(existingSesion);
            sesionCacheService.removeFromCache(id);
            return existingSesion;
        } else {
            return null;
        }
    }

    public void deleteSesion(String id) {
        sesionRepository.deleteById(id);
        sesionCacheService.removeFromCache(id);
    }
}
