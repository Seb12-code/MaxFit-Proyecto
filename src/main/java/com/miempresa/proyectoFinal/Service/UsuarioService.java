package com.miempresa.proyectoFinal.Service;

import com.miempresa.proyectoFinal.Model.Role;
import com.miempresa.proyectoFinal.Model.RoleName;
import com.miempresa.proyectoFinal.Model.Usuario;
import com.miempresa.proyectoFinal.Repository.RoleRepository;
import com.miempresa.proyectoFinal.Repository.UsuarioRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UsuarioService {

    private final UsuarioRegisterRepository usuarioRegisterRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UsuarioService(UsuarioRegisterRepository usuarioRegisterRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.usuarioRegisterRepository = usuarioRegisterRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository; //  Inicialización
    }

    @Transactional
    public void guardarUsuario(Usuario usuario) {
        try {
            // Log para debug
            System.out.println("=== INICIANDO GUARDADO DE USUARIO ===");
            System.out.println("Username: " + usuario.getUsername());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Roles: " + usuario.getRoles().size());

            // Verificar si el email ya existe
            if (usuario.getId_usuario() == null || (usuario.getId_usuario() != null && !usuarioRegisterRepository.findById(usuario.getId_usuario())
                    .map(u -> u.getEmail().equals(usuario.getEmail()))
                    .orElse(false))) {
                if (usuarioRegisterRepository.findByEmail(usuario.getEmail()).isPresent()) {
                    throw new RuntimeException("El email ya está registrado");
                }
            }


            // Verificar si el username ya existe (solo si es nuevo o si el username ha cambiado)
            if (usuario.getId_usuario() == null || (usuario.getId_usuario() != null && !usuarioRegisterRepository.findById(usuario.getId_usuario())
                    .map(u -> u.getUsername().equals(usuario.getUsername()))
                    .orElse(false))) {
                if (usuarioRegisterRepository.findByUsername(usuario.getUsername()).isPresent()) {
                    throw new RuntimeException("El nombre de usuario ya está registrado");
                }
            }


            // Codificar contraseña solo si es nueva o si ha sido modificada (no es la misma codificada)
            if (usuario.getPassword() != null && (usuario.getId_usuario() == null || !usuario.getPassword().startsWith("$2a$"))) {
                String passwordOriginal = usuario.getPassword();
                String passwordCodificada = passwordEncoder.encode(passwordOriginal);
                usuario.setPassword(passwordCodificada);
                System.out.println("Contraseña codificada exitosamente");
            } else if (usuario.getId_usuario() != null && usuario.getPassword() == null) {
                // Si es una actualización y la contraseña viene nula, mantener la existente
                usuarioRegisterRepository.findById(usuario.getId_usuario())
                        .ifPresent(u -> usuario.setPassword(u.getPassword()));
            }


            // Guardar usuario
            Usuario usuarioGuardado = usuarioRegisterRepository.save(usuario);
            System.out.println("Usuario guardado con ID: " + usuarioGuardado.getId_usuario());
            System.out.println("=== GUARDADO EXITOSO ===");

        } catch (Exception e) {
            System.err.println("=== ERROR AL GUARDAR USUARIO ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRegisterRepository.findAll();
    }


    public boolean existeEmail(String email) {
        return usuarioRegisterRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRegisterRepository.deleteById(id);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRegisterRepository.findById(id);
    }

    public List<Usuario> listarVendedores() {
        // Busca el rol de vendedor usando el enum RoleName
        Optional<Role> roleVendedorOpt = roleRepository.findByName(RoleName.ROLE_VENDEDOR);

        if (roleVendedorOpt.isPresent()) {
            Role roleVendedor = roleVendedorOpt.get();
            // Filtra los usuarios que tengan el rol de vendedor
            return usuarioRegisterRepository.findAll().stream()
                    .filter(usuario -> usuario.getRoles().contains(roleVendedor))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(); // Si el rol no existe, devuelve una lista vacía
    }
}