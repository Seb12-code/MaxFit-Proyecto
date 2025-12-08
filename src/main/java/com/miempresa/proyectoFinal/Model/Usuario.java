package com.miempresa.proyectoFinal.Model;

import com.miempresa.proyectoFinal.validator.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@UniqueEmail
public class Usuario implements UserDetails { // implementa UserDetails para representar un usuario autenticado dentro de la aplicación
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @Pattern(regexp = "\\d{9}", message = "El celular debe tener 9 dígitos")
    private String celular;



    // Relación One-to-Many con Venta: Un usuario (vendedor) puede tener muchas ventas
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventasRealizadas;

    // Relación One-to-Many con Cliente: Un usuario (vendedor) puede haber registrado muchos clientes
    @OneToMany(mappedBy = "registradoPor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cliente> clientesRegistrados;



    // Relación muchos a muchos con roles (Hara que se cre una tabla intermedia para la relación)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();



    // Implementación de UserDetails devuelve la lista de roles
    //Aquí se están convirtiendo los objetos role (de tipo personalizado) a objetos SimpleGrantedAuthority, que Spring entiende.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles; // ya son objetos GrantedAuthority
    }



    //Indica si la cuenta no ha expirado.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Indica si la cuenta no está bloqueada.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Indica si las credenciales (como contraseña) del usuario están vigentes.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Indica si el usuario está habilitado para iniciar sesión.
    @Override
    public boolean isEnabled() {
        return true;
    }


    // Constructores

    public Usuario(){}



    public Usuario(Long id_usuario, String username, String nombre, String password, String email, String celular, Set<Role> roles) {

        this.id_usuario = id_usuario;
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.email = email;
        this.celular = celular;
        this.roles = roles;
    }

    //Getters and Setter
    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
