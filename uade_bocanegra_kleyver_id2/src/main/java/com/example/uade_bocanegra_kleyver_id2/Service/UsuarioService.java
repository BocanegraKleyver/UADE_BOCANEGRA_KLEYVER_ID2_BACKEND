package com.example.uade_bocanegra_kleyver_id2.Service;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Redis.RedisCacheService;
import com.example.uade_bocanegra_kleyver_id2.Repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

public Usuario getUsuarioById(String id) {
    Usuario usuario = (Usuario) redisCacheService.getFromCache(id);
    if (usuario == null) {
        ObjectId objectId = new ObjectId(id); // Convertir el String a ObjectId
        Example<Usuario> example = Example.of(new Usuario(objectId.toString()),
                ExampleMatcher.matchingAny()); // Crear un ejemplo para buscar por ID
        usuario = usuarioRepository.findOne(example).orElse(null);
        if (usuario != null) {
            redisCacheService.addToCache(id, usuario);
        }
    }
    return usuario;
}

public Usuario getUsuarioByUsuario(String usuario) {
    return usuarioRepository.findByUsuario(usuario);
}

    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(new ObjectId().toString()); // Generar un nuevo ObjectId como String
        }
        Usuario savedUsuario = usuarioRepository.save(usuario);
        redisCacheService.addToCache(savedUsuario.getId(), savedUsuario);
        return savedUsuario;
    }

    public void deleteUsuario(String id) {
        ObjectId objectId = new ObjectId(id); // Convertir el String a ObjectId
        usuarioRepository.deleteById(objectId.toString()); // Convertir ObjectId a String
        redisCacheService.removeFromCache(id);
    }

    public Usuario updateUsuario(String id, Usuario usuario) {
        // Verificar si el usuario existe
        ObjectId objectId = new ObjectId(id); // Convertir el String a ObjectId
        Usuario existingUsuario = usuarioRepository.findById(objectId.toString()).orElse(null); // Convertir ObjectId a String
        if (existingUsuario != null) {
            // Actualizar los campos necesarios
            updateUsuarioFields(existingUsuario, usuario);
    
            // Guardar los cambios en la base de datos
            Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
            redisCacheService.addToCache(id, updatedUsuario); // Actualizar en caché si es necesario
            return updatedUsuario;
        } else {
            return null; // Devolver null si no se encuentra el usuario
        }
    }
    
    

    // Método para actualizar los campos de un usuario existente
    private void updateUsuarioFields(Usuario existingUsuario, Usuario updatedUsuario) {
        if (Objects.nonNull(updatedUsuario.getNombre())) {
            existingUsuario.setNombre(updatedUsuario.getNombre());
        }
        if (Objects.nonNull(updatedUsuario.getDireccion())) {
            existingUsuario.setDireccion(updatedUsuario.getDireccion());
        }
        if (Objects.nonNull(updatedUsuario.getDocumentoIdentidad())) {
            existingUsuario.setDocumentoIdentidad(updatedUsuario.getDocumentoIdentidad());
        }
        if (Objects.nonNull(updatedUsuario.getCategoria())) {
            existingUsuario.setCategoria(updatedUsuario.getCategoria());
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
