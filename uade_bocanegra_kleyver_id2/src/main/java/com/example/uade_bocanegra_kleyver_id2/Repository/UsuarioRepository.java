package com.example.uade_bocanegra_kleyver_id2.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    // Método para buscar un usuario por su nombre de usuario
    Usuario findByUsuario(String usuario);
}
