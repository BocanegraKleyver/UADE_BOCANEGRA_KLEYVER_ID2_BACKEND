package com.example.uade_bocanegra_kleyver_id2.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "usuario")
public class Usuario {

    @Id
    private String id; // Cambiar ObjectId a String

    private String nombre;
    private String direccion;
    private String documentoIdentidad;
    private String categoria;
    private String usuario;
    private String password;
    private String email;

    // Constructor predeterminado
    public Usuario() {
    }

    // Constructor con solo el id como argumento
    public Usuario(String id) {
        this.id = id;
    }

    // Constructor con todos los atributos
    @JsonCreator
    public Usuario(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("direccion") String direccion,
            @JsonProperty("documentoIdentidad") String documentoIdentidad,
            @JsonProperty("categoria") String categoria,
            @JsonProperty("usuario") String usuario,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.documentoIdentidad = documentoIdentidad;
        this.categoria = categoria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
