package com.miempresa.proyectoFinal.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/auth/login")
    public String mostrarFormularioLogin() {
        logger.info("Accediendo a la página de login.");
        return "auth/login"; // ruta
    }

    @GetMapping("/")
    public String mostrarIndex() {
        logger.info("Accediendo a la página principal (dashboard).");
        return "dashboard"; // Vista principal después del login
    }
}
