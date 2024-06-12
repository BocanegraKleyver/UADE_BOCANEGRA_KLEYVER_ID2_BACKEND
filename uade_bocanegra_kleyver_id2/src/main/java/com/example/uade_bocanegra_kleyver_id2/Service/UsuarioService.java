package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Carrito;
import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Redis.CacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CacheService<Usuario> usuarioCacheService;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(String id) {
        Usuario usuario = usuarioCacheService.getFromCache(id);
        if (usuario == null) {
            usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario != null) {
                usuarioCacheService.addToCache(id, usuario);
            }
        }
        return usuario;
    }

    public Usuario getUsuarioByUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(java.util.UUID.randomUUID().toString());
        }
        Usuario savedUsuario = usuarioRepository.save(usuario);
        usuarioCacheService.addToCache(savedUsuario.getId(), savedUsuario); 
        return savedUsuario;
    }

    public void deleteUsuario(String id) {
        usuarioRepository.deleteById(id);
        usuarioCacheService.removeFromCache(id);
    }

    public Usuario updateUsuario(String id, Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(id).orElse(null);
        if (existingUsuario != null) {
            updateUsuarioFields(existingUsuario, usuario);
            Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
            usuarioCacheService.removeFromCache(id); 
            usuarioCacheService.addToCache(updatedUsuario.getId(), updatedUsuario);
            return updatedUsuario;
        } else {
            return null;
        }
    }

    public boolean isCarritoActivo(String usuarioId) {
        Optional<Carrito> carritoOptional = carritoService.obtenerCarritoPorUsuarioId(usuarioId);
        return carritoOptional.isPresent() && carritoOptional.get().isActivo();
    }


    public Usuario autenticarUsuario(String nombreUsuario, String password) {
        return usuarioRepository.findByUsuarioAndPassword(nombreUsuario, password);
    }

    private void updateUsuarioFields(Usuario existingUsuario, Usuario updatedUsuario) {
        if (Objects.nonNull(updatedUsuario.getNombre())) {
            existingUsuario.setNombre(updatedUsuario.getNombre());
        }
        if (Objects.nonNull(updatedUsuario.getApellido())){
            existingUsuario.setApellido(updatedUsuario.getApellido());
        }
        if (Objects.nonNull(updatedUsuario.getDireccion())) {
            existingUsuario.setDireccion(updatedUsuario.getDireccion());
        }
        if (Objects.nonNull(updatedUsuario.getDocumentoIdentidad())) {
            existingUsuario.setDocumentoIdentidad(updatedUsuario.getDocumentoIdentidad());
        }
        if (Objects.nonNull(updatedUsuario.getUsuario())) {
            existingUsuario.setUsuario(updatedUsuario.getUsuario());
        }
        if (Objects.nonNull(updatedUsuario.getPassword())) {
            existingUsuario.setPassword(updatedUsuario.getPassword());
        }
        if (Objects.nonNull(updatedUsuario.getEmail())) {
            existingUsuario.setEmail(updatedUsuario.getEmail());
        }
    }
}
