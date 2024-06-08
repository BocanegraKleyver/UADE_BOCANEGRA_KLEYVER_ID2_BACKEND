package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
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
    private SesionCacheService sesionCacheService; // Cambiado al servicio de caché de sesión

    public List<Sesion> getAllSesiones() {
        return sesionRepository.findAll();
    }

    public Sesion getSesionById(String id) {
        Sesion sesion = sesionCacheService.getFromCache(id);
        if (sesion == null) {
            ObjectId objectId = new ObjectId(id);
            sesion = sesionRepository.findById(objectId).orElse(null);
            if (sesion != null) {
                sesionCacheService.addToCache(id, sesion);
            }
        }
        return sesion;
    }
    

    // public Sesion iniciarSesion(Sesion sesion) {
    //     // Setear la fecha de inicio
    //     sesion.setFechaInicio(new Date());
        
    //     // Guardar la sesión
    //     Sesion savedSesion = sesionRepository.save(sesion);
        
    //     // Agregar la sesión a la caché
    //     sesionCacheService.addToCache(savedSesion.getId().toString(), savedSesion);

    //     return savedSesion;
    // }
    public Sesion iniciarSesion(Usuario usuario) {
        System.out.println("Iniciando sesión para el usuario: " + usuario.getUsuario());
        
        // Crear una nueva sesión
        Sesion sesion = new Sesion();
        
        // Obtener el ID del usuario
        ObjectId usuarioId = (ObjectId) usuario.getId();
        System.out.println("ID del usuario: " + usuarioId);
        
        // Setear el ID del usuario en la sesión
        sesion.setUsuarioId(usuarioId);
        
        // Setear la fecha de inicio
        sesion.setFechaInicio(new Date());
        
        // Guardar la sesión
        Sesion savedSesion = sesionRepository.save(sesion);
        System.out.println("Sesión iniciada y guardada correctamente: " + savedSesion.getId());
        
        // Agregar la sesión a la caché
        sesionCacheService.addToCache(savedSesion.getId().toString(), savedSesion);
    
        return savedSesion;
    }
    

    public Sesion cerrarSesion(String id) {
        ObjectId objectId = new ObjectId(id);
        Sesion existingSesion = sesionRepository.findById(objectId).orElse(null);
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
        ObjectId objectId = new ObjectId(id); // Convertir el String ID a ObjectId
        sesionRepository.deleteById(objectId);
        sesionCacheService.removeFromCache(id);
    }
}
