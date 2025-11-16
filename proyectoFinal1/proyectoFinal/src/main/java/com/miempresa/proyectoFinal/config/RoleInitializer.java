package com.miempresa.proyectoFinal.config;

import com.miempresa.proyectoFinal.Model.Role;
import com.miempresa.proyectoFinal.Model.RoleName;
import com.miempresa.proyectoFinal.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            // Itera sobre todos los valores de tu enum RoleName
            for (RoleName roleNameEnum : RoleName.values()) {
                // Busca el rol por su nombre (que es de tipo RoleName)
                if (roleRepository.findByName(roleNameEnum).isEmpty()) {
                    // Si no existe, lo guarda
                    roleRepository.save(new Role(roleNameEnum));
                }
            }
        };
    }
}