package com.miempresa.proyectoFinal.validator;

import com.miempresa.proyectoFinal.Model.Usuario;
import com.miempresa.proyectoFinal.Repository.UsuarioRegisterRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Usuario> {

    private UsuarioRegisterRepository usuarioRegisterRepository;

    public UniqueEmailValidator() {
        // Constructor por defecto requerido por Hibernate Validator
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRegisterRepository usuarioRegisterRepository) {
        this.usuarioRegisterRepository = usuarioRegisterRepository;
    }

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return true; // Se asume que @NotBlank/@Email se encargan de la obligatoriedad y formato.
        }

        String email = usuario.getEmail();
        Long userId = usuario.getId_usuario();

        if (usuarioRegisterRepository == null) { // inyeccion del repositorio
            return true; //
        }

        java.util.Optional<Usuario> usuarioExistente = usuarioRegisterRepository.findByEmail(email);

        if (usuarioExistente.isPresent()) {
            // Si el email existe, es valido solo si pertenece al usuario que se está actualizando
            boolean esMismoUsuario = userId != null && usuarioExistente.get().getId_usuario().equals(userId);
            if (!esMismoUsuario) {
                context.disableDefaultConstraintViolation(); // Deshabilitar el mensaje de error global por defecto
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()) // Usar el mensaje de la anotación
                        .addPropertyNode("email") // Asociar el error con el campo 'email'
                        .addConstraintViolation(); // Construir la violacion
            }
            return esMismoUsuario;
        }

        return true; // El email no existe entonces no es unico

    }
}

