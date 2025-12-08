package com.miempresa.proyectoFinal.Repository;

import com.miempresa.proyectoFinal.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestionUsuarioRepository extends JpaRepository<Usuario, Long> {
}
