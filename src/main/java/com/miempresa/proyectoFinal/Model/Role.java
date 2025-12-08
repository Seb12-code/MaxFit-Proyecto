package com.miempresa.proyectoFinal.Model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="roles")

//implements GrantedAuthority
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private RoleName name;

    public Role(){ }

    public Role(RoleName name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return name.name(); //
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}

