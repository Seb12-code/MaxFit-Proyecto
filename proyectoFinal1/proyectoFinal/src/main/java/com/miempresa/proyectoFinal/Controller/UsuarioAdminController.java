package com.miempresa.proyectoFinal.Controller;


import com.miempresa.proyectoFinal.Model.Usuario;
import com.miempresa.proyectoFinal.Service.RoleService;
import com.miempresa.proyectoFinal.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/admin/gestionUsuario")
public class UsuarioAdminController {

    private final UsuarioService usuarioService;
    private final RoleService roleService;

    @Autowired
    public UsuarioAdminController(UsuarioService usuarioService, RoleService roleService) {
        this.usuarioService = usuarioService;
        this.roleService = roleService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(@RequestParam(name = "exito", required = false) String exito,
                                            @RequestParam(name = "id", required = false) Long id,
                                            Model model) {

        Usuario usuario;
        if (id != null) {

            usuario = usuarioService.obtenerUsuarioPorId(id).orElse(new Usuario()); //
        } else {
            usuario = new Usuario();
        }


        model.addAttribute("usuario", usuario);
        model.addAttribute("listaRoles", roleService.listarRoles());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());

        if (exito != null) {
            model.addAttribute("exito", exito);
        }

        return "admin/usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuario", result);
            redirectAttributes.addFlashAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("error", "Revise los campos del formulario.");
            return "redirect:/admin/gestionUsuario/nuevo";
        }

        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            redirectAttributes.addFlashAttribute("usuario", usuario);
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un rol.");
            return "redirect:/admin/gestionUsuario/nuevo";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("exito", "Usuario guardado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("usuario", usuario);
        }

        return "redirect:/admin/gestionUsuario/nuevo";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam Long id) {
        return "redirect:/admin/gestionUsuario/nuevo?id=" + id;
    }

    @GetMapping("/eliminar")
    public String eliminarUsuario(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("exito", "Usuario eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/admin/gestionUsuario/nuevo";
    }
}