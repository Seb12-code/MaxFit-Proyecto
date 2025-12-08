package com.miempresa.proyectoFinal.config;

import com.miempresa.proyectoFinal.Model.Role;
import com.miempresa.proyectoFinal.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String id){
        return roleRepository.findById(Long.valueOf(id)).orElse(null);
    }
}

