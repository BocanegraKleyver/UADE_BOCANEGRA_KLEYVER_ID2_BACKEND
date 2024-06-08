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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Redis.ContadorVisitasService;
import com.example.uade_bocanegra_kleyver_id2.Service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el puerto 3000
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContadorVisitasService contadorVisitasService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable String id) {
        contadorVisitasService.incrementarVisita("usuario:" + id); // Incrementa el contador de visitas
        return usuarioService.getUsuarioById(id);
    }

    @PostMapping("/register") // Endpoint para registro de usuarios
    public ResponseEntity<Usuario> registerUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.saveUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario) {
        boolean autenticacionExitosa = verificarCredenciales(usuario);

        if (autenticacionExitosa) {
            return ResponseEntity.ok(Map.of("message", "Inicio de sesión exitoso"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
    }

    // Método para verificar las credenciales
    private boolean verificarCredenciales(Usuario usuario) {
        // Aquí deberías implementar la lógica para verificar si las credenciales son correctas
        // Por ejemplo, podrías consultar en la base de datos si existe un usuario con las credenciales proporcionadas
        // Retorna true si las credenciales son válidas, false si no lo son
        // Aquí solo pongo un ejemplo simple, debes adaptarlo a tu lógica real
        Usuario usuarioEncontrado = usuarioService.getUsuarioByUsuario(usuario.getUsuario());
        return usuarioEncontrado != null && usuarioEncontrado.getPassword().equals(usuario.getPassword());
    }
    


    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable String id) {
        usuarioService.deleteUsuario(id);
    }
}
