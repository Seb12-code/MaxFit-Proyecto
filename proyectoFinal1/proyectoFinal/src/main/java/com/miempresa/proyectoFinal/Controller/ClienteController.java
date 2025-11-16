package com.miempresa.proyectoFinal.Controller;

import com.miempresa.proyectoFinal.Model.Cliente;
import com.miempresa.proyectoFinal.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("clientes", clienteService.listarClientes());
        return "vendedor/clientes";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return "redirect:/clientes";
    }

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("clientes", clienteService.listarClientes());
        return "vendedor/clientes";
    }

    @GetMapping("/eliminar")
    public String eliminarCliente(@RequestParam("id") Long id) {
        clienteService.eliminarCliente(id);
        return "redirect:/clientes";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Long id, Model model) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clienteService.listarClientes());
        return "vendedor/clientes";
    }
}
