package com.miempresa.proyectoFinal.Controller;

import com.miempresa.proyectoFinal.Model.Producto;
import com.miempresa.proyectoFinal.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("productos", productoService.listarProductos());
        return "vendedor/productos";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("productos", productoService.listarProductos());
        return "vendedor/productos"; // ruta
    }

    @GetMapping("/eliminar")
    public String eliminarProducto(@RequestParam("id") Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam("id") Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("productos", productoService.listarProductos());
        return "vendedor/productos";
    }
}