package com.example.uade_bocanegra_kleyver_id2.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade_bocanegra_kleyver_id2.Entity.Sesion;
import com.example.uade_bocanegra_kleyver_id2.Service.SesionService;

@RestController
@RequestMapping("/api/sesion")
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

    @PostMapping("/iniciar")
    public Sesion iniciarSesion(@RequestBody Sesion sesion) {
        return sesionService.iniciarSesion(sesion);
    }

    @DeleteMapping("/{id}")
    public void deleteSesion(@PathVariable String id) {
        sesionService.deleteSesion(id);
    }
}
