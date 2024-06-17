package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;
import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Entity.UsuarioActividad;
import com.example.uade_bocanegra_kleyver_id2.Redis.ContadorVisitasService;
import com.example.uade_bocanegra_kleyver_id2.Service.CarritoService;
import com.example.uade_bocanegra_kleyver_id2.Service.SesionService;
import com.example.uade_bocanegra_kleyver_id2.Service.UsuarioActividadService;
import com.example.uade_bocanegra_kleyver_id2.Service.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:3000") // Habilitar CORS para permitir solicitudes desde el frontend en el
                                                // puerto 3000
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SesionService sesionService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioActividadService usuarioActividadService;

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
    public ResponseEntity<Map<String, Object>> registerUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.saveUsuario(usuario);
        carritoService.crearCarrito(nuevoUsuario.getId());
        carritoService.marcarCarritoComoCerrado(nuevoUsuario.getId());
        Sesion sesion = sesionService.iniciarSesion(nuevoUsuario);
        UsuarioActividad nuevaActividad = usuarioActividadService.registrarActividad(sesion.getId(),
                "Usuario Registrado.");
        // Cerrar sesión después del registro
        sesionService.cerrarSesion(sesion.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("usuario", nuevoUsuario, "actividad", nuevaActividad));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Usuario usuario) {
        boolean autenticacionExitosa = verificarCredenciales(usuario);

        if (autenticacionExitosa) {
            Usuario usuarioAutenticado = usuarioService.autenticarUsuario(usuario.getUsuario(), usuario.getPassword());
            Optional<Carrito> carritoOptional = carritoService.obtenerCarritoPorUsuarioId(usuarioAutenticado.getId()); // Asegúrate
                                                                                                                       // de
                                                                                                                       // que
                                                                                                                       // aquí
                                                                                                                       // se
                                                                                                                       // esté
                                                                                                                       // utilizando
                                                                                                                       // el
                                                                                                                       // ID
                                                                                                                       // correcto
            if (!carritoOptional.isPresent()) {
                carritoService.crearCarrito(usuarioAutenticado.getId());
                System.out.println("Carrito creado al iniciar sesión para usuario: " + usuarioAutenticado.getId());
            }
            Sesion sesion = sesionService.iniciarSesion(usuarioAutenticado);
            UsuarioActividad nuevaActividad = usuarioActividadService.registrarActividad(sesion.getId(),
                    "Inició sesión");
            return ResponseEntity.ok(Map.of("usuario", usuarioAutenticado, "actividad", nuevaActividad));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUsuario(@RequestBody Usuario usuario) {
        String usuarioId = usuario.getId();
        carritoService.marcarCarritoComoCerrado(usuarioId);
        Optional<Carrito> carritoOptional = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
        carritoOptional.ifPresent(carrito -> System.out
                .println("Carrito " + (carrito.isActivo() ? "cambiado de estado a cerrado" : "marcado como cerrado")
                        + " para usuario: " + usuarioId + ", ID del carrito: " + carrito.getId()));

        Sesion sesionActiva = sesionService.getSesionActivaByUsuarioId(usuarioId);

        if (sesionActiva != null) {
            usuarioActividadService.registrarActividad(sesionActiva.getId(), "Cerró sesión");
            System.out.println("Usuario con ID: " + usuarioId + " ha cerrado su sesión");
            LocalDateTime now = LocalDateTime.now();
            Date fechaFin = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            sesionActiva.setFechaFin(fechaFin);
            sesionService.cerrarSesion(sesionActiva.getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una sesión activa para el usuario");
        }
        return ResponseEntity.ok().build();
    }

    private boolean verificarCredenciales(Usuario usuario) {
        Usuario usuarioEncontrado = usuarioService.getUsuarioByUsuario(usuario.getUsuario());
        return usuarioEncontrado != null && usuarioEncontrado.getPassword().equals(usuario.getPassword());
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuario);
        Sesion sesionActiva = sesionService.getSesionActivaByUsuarioId(id);
        if (sesionActiva != null) {
            usuarioActividadService.registrarActividad(sesionActiva.getId(), "Actualizó sus datos");
        }
        return usuarioActualizado;
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable String id) {
        usuarioService.deleteUsuario(id);
    }

}
