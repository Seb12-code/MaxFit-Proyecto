package com.miempresa.proyectoFinal.Service;

import com.miempresa.proyectoFinal.Model.Role;
import com.miempresa.proyectoFinal.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> listarRoles(){
        return roleRepository.findAll();
    }
}
