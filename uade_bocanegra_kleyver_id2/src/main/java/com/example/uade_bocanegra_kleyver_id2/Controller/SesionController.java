package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.util.List;
import java.util.Map;

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

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;
import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Service.SesionService;

@RestController
@RequestMapping("/api/sesion")
@CrossOrigin(origins = "http://localhost:3000")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @GetMapping
    public List<Sesion> getAllSesiones() {
        return sesionService.getAllSesiones();
    }

    @GetMapping("/{id}")
    public Sesion getSesionById(@PathVariable String id) {
        return sesionService.getSesionById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody Usuario usuario) {
        Sesion sesion = sesionService.iniciarSesion(usuario);
        if (sesion != null) {
            return ResponseEntity.ok(sesion);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
    }

    @PostMapping("/cerrar/{id}")
    public ResponseEntity<?> cerrarSesion(@PathVariable String id) {
        Sesion sesionCerrada = sesionService.cerrarSesion(id);
        if (sesionCerrada != null) {
            return ResponseEntity.ok(sesionCerrada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSesion(@PathVariable String id) {
        sesionService.deleteSesion(id);
        return ResponseEntity.noContent().build();
    }
}
