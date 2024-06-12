package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional; // Importa Optional de java.util
import java.util.concurrent.TimeUnit;

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
        Sesion sesion = new Sesion(usuario.getId(), new Date(), null,null,0); // Usar ID de usuario y fecha actual para iniciar sesión
        Sesion savedSesion = sesionRepository.save(sesion);
        sesionCacheService.addToCache(savedSesion.getId(), savedSesion);
        return savedSesion;
    }

    public Sesion cerrarSesion(String id) {
        Optional<Sesion> existingSesionOptional = sesionRepository.findById(id);
        if (existingSesionOptional.isPresent()) {
            Sesion existingSesion = existingSesionOptional.get();
            existingSesion.setFechaFin(new Date());

            //2 - No uso minutos como ejemplo porque la demostracion es de menor tiempo. Usare segundos --> 30seg para low, 60 seg Medium, +60seg TOP
            // Saco la categoria por sesion. Como quiero recuperar
            long durationInMillis = existingSesion.getFechaFin().getTime() - existingSesion.getFechaInicio().getTime();
            long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis);
            if (durationInSeconds <= 30) {
                existingSesion.setCategoriaSesion("LOW");
            } else if (durationInSeconds <= 60) {
                existingSesion.setCategoriaSesion("MEDIUM");
            } else {
                existingSesion.setCategoriaSesion("TOP");
            }
            existingSesion.setCategoriaSesionTiempo(durationInSeconds);
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
