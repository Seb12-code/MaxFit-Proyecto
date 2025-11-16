package com.miempresa.proyectoFinal.Repository;

import com.miempresa.proyectoFinal.Model.Role;
import com.miempresa.proyectoFinal.Model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName nombre); //
}