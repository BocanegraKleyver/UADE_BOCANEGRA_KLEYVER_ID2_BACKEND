package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.UsuarioActividad;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.UsuarioActividadRepository;

@Service
public class UsuarioActividadService {

    @Autowired
    private UsuarioActividadRepository usuarioActividadRepository;

    @Autowired
    private CacheService<UsuarioActividad> usuarioActividadCacheService;

    public List<UsuarioActividad> getAllUsuarioActividades() {
        return usuarioActividadRepository.findAll();
    }

    public UsuarioActividad getUsuarioActividadById(String id) {
        return usuarioActividadRepository.findById(id).orElse(null);
    }

    public UsuarioActividad registrarActividad(String sesionId, String actividad) {
        UsuarioActividad usuarioActividad = new UsuarioActividad();
        usuarioActividad.setSesionId(sesionId); // Establecer el ID de la sesión
        usuarioActividad.setActividad(actividad); // Establecer la actividad
        usuarioActividad.setFecha(new Date());
        UsuarioActividad savedUsuarioActividad = usuarioActividadRepository.save(usuarioActividad);
        usuarioActividadCacheService.addToCache(savedUsuarioActividad.getId(), savedUsuarioActividad);
        return savedUsuarioActividad;
    }

    public void deleteUsuarioActividad(String id) {
        usuarioActividadRepository.deleteById(id);
        usuarioActividadCacheService.removeFromCache(id);
    }

    public List<UsuarioActividad> obtenerActividadesPorSesion(String sesionId) {
        return usuarioActividadRepository.findBySesionId(sesionId);
    }

}
