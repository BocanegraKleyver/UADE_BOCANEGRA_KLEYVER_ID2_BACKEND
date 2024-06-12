package com.example.uade_bocanegra_kleyver_id2.db.MongoDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.uade_bocanegra_kleyver_id2.Entity.Usuario;
import com.example.uade_bocanegra_kleyver_id2.Repository.UsuarioRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        
        Usuario kleyver = usuarioRepository.findByUsuario("kleyver");
        if (kleyver == null) {
            
            kleyver = new Usuario();
            kleyver.setNombre("Kleyver");
            kleyver.setApellido("Bocanegra");
            kleyver.setDireccion("Valentin Gomez 3545");
            kleyver.setDocumentoIdentidad("1116590");
            kleyver.setUsuario("kleyver");
            kleyver.setPassword("123456"); // Recuerda usar un método seguro para manejar contraseñas
            kleyver.setEmail("kbocanegracisneros@uade.edu.ar");
            kleyver.setRole("admin");
            usuarioRepository.save(kleyver);
            System.out.println("Usuario kleyver creado como administrador.");
        } else {
            System.out.println("Usuario kleyver ya existe.");
        }
    }
}