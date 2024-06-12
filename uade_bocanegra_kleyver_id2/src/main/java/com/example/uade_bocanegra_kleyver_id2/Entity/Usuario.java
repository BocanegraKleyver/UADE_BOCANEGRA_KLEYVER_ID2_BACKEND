package com.example.uade_bocanegra_kleyver_id2.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "usuario")
public class Usuario {

    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String direccion;
    private String documentoIdentidad;
    private String usuario;
    private String password;
    private String email;

    
    public Usuario() {
    }

    
    @JsonCreator
    public Usuario(
            @JsonProperty("id") String id,
            @JsonProperty("nombre") String nombre,
            @JsonProperty("apellido") String apellido,
            @JsonProperty("direccion") String direccion,
            @JsonProperty("documentoIdentidad") String documentoIdentidad,
            @JsonProperty("usuario") String usuario,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.documentoIdentidad = documentoIdentidad;
        this.usuario = usuario;
        this.password = password;
        this.email = email;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
