package com.miempresa.proyectoFinal.Model;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="nombre")
    private String nombreCompleto;
    private String dni;

    @Column(name ="telefono")
    private String celular; //Se cambio el nombre de la variable de telefono a celular.
    private String direccion;
    private String email;

    // Relación Many-to-One con Usuario: Muchos clientes pueden ser registrados por un usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario_registro") // Columna de la FK en la tabla 'clientes'
    private Usuario registradoPor; // El usuario que registro a este cliente

    //  Constructores
    public Cliente() {}

    //Cambios realizados aqui
    public Cliente(String nombreCompleto, String dni, String celular, String direccion, String email, Usuario registradoPor) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.celular = celular; //Se cambio el nombre de la variable de telefono a celular.
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

    public String getCelular() { //Se cambio el nombre del metodo de getTelefono a getCelular.
        return celular;
    }

    public void setCelular(String celular) { //Se cambio el nombre del metodo de setTelefono a setCelular.
        this.celular = celular;
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

