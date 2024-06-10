package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade_bocanegra_kleyver_id2.Entity.UsuarioActividad;
import com.example.uade_bocanegra_kleyver_id2.Service.UsuarioActividadService;

@RestController
@RequestMapping("/api/usuarioactividad")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el puerto 3000
public class UsuarioActividadController {

    @Autowired
    private UsuarioActividadService usuarioActividadService;

    @GetMapping
    public List<UsuarioActividad> getAllUsuarioActividades() {
        return usuarioActividadService.getAllUsuarioActividades();
    }

    @GetMapping("/{id}")
    public UsuarioActividad getUsuarioActividadById(@PathVariable String id) {
        return usuarioActividadService.getUsuarioActividadById(id);
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioActividad> registrarActividad(@RequestBody UsuarioActividad usuarioActividad) {
        String sesionId = usuarioActividad.getSesionId(); // Obtener el ID de la sesión desde el objeto UsuarioActividad
        String actividad = usuarioActividad.getActividad(); // Obtener la actividad del objeto UsuarioActividad
    
        UsuarioActividad nuevaActividad = usuarioActividadService.registrarActividad(sesionId, actividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuarioActividad(@PathVariable String id) {
        usuarioActividadService.deleteUsuarioActividad(id);
        return ResponseEntity.ok().build();
    }
}
