package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.uade_bocanegra_kleyver_id2.Service.UsuarioService; // Importa la entidad Usuario

@RestController
@RequestMapping("/api/sesion")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @Autowired
    private UsuarioService usuarioService; // Autowire UsuarioService

    // Obtener todas las sesiones
    @GetMapping
    public List<Sesion> getAllSesiones() {
        return sesionService.getAllSesiones();
    }

    // Obtener una sesión por su ID
    @GetMapping("/{id}")
    public Sesion getSesionById(@PathVariable String id) {
        return sesionService.getSesionById(id);
    }

@PostMapping("/login")
public ResponseEntity<?> iniciarSesion(@RequestBody Usuario usuario) {
    // Llama al servicio de usuario para verificar las credenciales
    Usuario usuarioAutenticado = usuarioService.autenticarUsuario(usuario.getUsuario(), usuario.getPassword());
    if (usuarioAutenticado != null) {
        // Si el usuario es autenticado, inicia sesión y retorna la sesión creada
        Sesion sesion = sesionService.iniciarSesion(usuarioAutenticado);
        return ResponseEntity.ok(sesion); // Retornar la sesión creada
    } else {
        // Si las credenciales son incorrectas, devuelve un mensaje de error
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario o contraseña incorrectos"));
    }
}

    
    // Cerrar una sesión existente
    @PostMapping("/cerrar/{id}")
    public Sesion cerrarSesion(@PathVariable String id) {
        return sesionService.cerrarSesion(id);
    }

    // Eliminar una sesión por su ID
    @DeleteMapping("/{id}")
    public void deleteSesion(@PathVariable String id) {
        sesionService.deleteSesion(id);
    }
}
