package com.miempresa.proyectoFinal.Model;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String direccion;
    private String email;

    // Relación Many-to-One con Usuario: Muchos clientes pueden ser registrados por un usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario_registro") // Columna de la FK en la tabla 'clientes'
    private Usuario registradoPor; // El usuario que registro a este cliente

    //  Constructores
    public Cliente() {}

    public Cliente(String nombreCompleto, String dni, String telefono, String direccion, String email, Usuario registradoPor) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.registradoPor = registradoPor;
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Usuario registradoPor) {
        this.registradoPor = registradoPor;
    }

}

