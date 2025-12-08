package com.miempresa.proyectoFinal.Repository;

import com.miempresa.proyectoFinal.Model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRegisterRepository extends JpaRepository<Usuario, Long> {
    Boolean existsByEmail(String email); // Necesario para validar que es unico

    Optional<Usuario> findByEmail(String email);

    Boolean existsByUsername(String username);
    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByUsername(String username);
}

