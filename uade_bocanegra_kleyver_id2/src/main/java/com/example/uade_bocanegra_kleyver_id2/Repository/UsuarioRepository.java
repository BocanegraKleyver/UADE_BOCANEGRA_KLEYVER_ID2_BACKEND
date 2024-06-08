package com.example.uade_bocanegra_kleyver_id2.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {
    // Método para buscar un usuario por su nombre de usuario
    Usuario findByUsuario(String usuario);

    // Método para autenticar un usuario por usuario y contraseña
    Usuario findByUsuarioAndPassword(String usuario, String password);
}
