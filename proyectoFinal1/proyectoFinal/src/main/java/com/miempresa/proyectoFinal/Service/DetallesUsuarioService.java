package com.miempresa.proyectoFinal.Service;

import com.miempresa.proyectoFinal.Model.Usuario;
import com.miempresa.proyectoFinal.Repository.UsuarioRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DetallesUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRegisterRepository usuarioRegisterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRegisterRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return usuario;
    }

}