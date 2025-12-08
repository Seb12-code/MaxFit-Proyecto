package com.miempresa.proyectoFinal.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistasController {

    @GetMapping("/dashboard")
    public String mostrarDashboard() {
        return "dashboard"; // ruta raiz
    }

    @GetMapping("/usuarios")
    public String mostrarGestionUsuarios() {
        return "usuarios";
    }




}
