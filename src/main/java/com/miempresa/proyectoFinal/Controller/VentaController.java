package com.miempresa.proyectoFinal.Controller;

import com.miempresa.proyectoFinal.Model.*;
import com.miempresa.proyectoFinal.Repository.ClienteRepository;
import com.miempresa.proyectoFinal.Repository.ProductoRepository;
import com.miempresa.proyectoFinal.Repository.VentaRepository;

import com.miempresa.proyectoFinal.Service.UsuarioService;
import com.miempresa.proyectoFinal.Service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/ventas")
@SessionAttributes({"cliente", "detalleVenta", "total", "tipoComprobante", "producto"})
public class VentaController {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("detalleVenta")
    public List<VentaDetalle> detalleVenta() {
        return new ArrayList<>();
    }

    @ModelAttribute("total")
    public Double total() {
        return 0.0;
    }

    @ModelAttribute("producto")
    public Producto producto() {
        return new Producto();
    }

    @GetMapping // Muestra el formulario de ventas
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("cliente")) model.addAttribute("cliente", new Cliente());
        if (!model.containsAttribute("producto")) model.addAttribute("producto", new Producto());
        if (!model.containsAttribute("tipoComprobante")) model.addAttribute("tipoComprobante", "");

        model.addAttribute("vendedores", usuarioService.listarVendedores());
        return "vendedor/ventas"; //  Ruta completa
    }

    @PostMapping("/buscarCliente") // Busca un cliente por DNI
    public String buscarCliente(@RequestParam String dni, Model model) {
        Optional<Cliente> clienteOpt = clienteRepo.findByDni(dni);
        clienteOpt.ifPresent(cliente -> model.addAttribute("cliente", cliente));
        model.addAttribute("vendedores", usuarioService.listarVendedores());
        return "vendedor/ventas"; //  Ruta completa
    }

    @PostMapping("/buscarProducto") // Busca un producto por nombre
    public String buscarProducto(@RequestParam String nombre, Model model) {
        List<Producto> productos = productoRepo.findByNombreContainingIgnoreCase(nombre);
        if (!productos.isEmpty()) {
            model.addAttribute("producto", productos.get(0));
        }
        model.addAttribute("vendedores", usuarioService.listarVendedores());
        return "vendedor/ventas"; //  Ruta completa
    }

    @PostMapping("/agregarItem") // Agrega un item al detalle de la venta
    public String agregarItem(@RequestParam int cantidad,
                              @ModelAttribute("producto") Producto producto,
                              @ModelAttribute("detalleVenta") List<VentaDetalle> detalle,
                              Model model) {

        if (producto != null && cantidad > 0) {
            VentaDetalle item = new VentaDetalle();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setSubtotal(producto.getPrecio() * cantidad);
            detalle.add(item);
        }

        double total = detalle.stream().mapToDouble(VentaDetalle::getSubtotal).sum();
        model.addAttribute("total", total);
        model.addAttribute("vendedores", usuarioService.listarVendedores());
        return "vendedor/ventas"; // Ruta completa
    }

    @PostMapping("/registrar") // Registra la venta
    public String registrarVenta(@ModelAttribute("cliente") Cliente cliente,
                                 @ModelAttribute("detalleVenta") List<VentaDetalle> detalle,
                                 @ModelAttribute("total") Double totalVenta,
                                 @RequestParam("tipoComprobante") String tipoComprobante,
                                 @RequestParam("vendedorId") Long vendedorId,
                                 Model model,
                                 SessionStatus sessionStatus) {

        if (detalle == null || detalle.isEmpty()) {
            model.addAttribute("error", "No se puede registrar una venta sin productos.");
            model.addAttribute("vendedores", usuarioService.listarVendedores());
            return "vendedor/ventas"; // Ruta completa
        }

        Optional<Usuario> vendedorOpt = usuarioService.obtenerUsuarioPorId(vendedorId);
        if (vendedorOpt.isEmpty()) {
            model.addAttribute("error", "Debe seleccionar un vendedor válido.");
            model.addAttribute("vendedores", usuarioService.listarVendedores());
            return "vendedor/ventas"; //  Ruta completa
        }
        Usuario vendedorSeleccionado = vendedorOpt.get();

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDate.now());
        venta.setTipoComprobante(tipoComprobante);
        venta.setDetalles(detalle);
        venta.setVendedor(vendedorSeleccionado);

        double subtotal = totalVenta;
        double igv = "factura".equalsIgnoreCase(tipoComprobante) ? subtotal * 0.18 : 0.0;

        venta.setIgv(igv);
        venta.setSubtotal(subtotal);
        venta.setTotal(subtotal + igv);

        long cantidadVentas = ventaRepository.count() + 1;
        String prefijo = tipoComprobante.equalsIgnoreCase("factura") ? "F" : "B";
        String numeroComprobante = prefijo + String.format("%06d", cantidadVentas);
        venta.setNumeroComprobante(numeroComprobante);

        ventaService.guardarVenta(venta);

        for (VentaDetalle item : detalle) {
            item.setVenta(venta);
        }

        sessionStatus.setComplete();

        model.addAttribute("ventaRegistrada", venta);
        model.addAttribute("exito", "Venta registrada exitosamente!");

        model.addAttribute("cliente", new Cliente());
        model.addAttribute("producto", new Producto());
        model.addAttribute("detalleVenta", new ArrayList<>());
        model.addAttribute("total", 0.0);
        model.addAttribute("tipoComprobante", "");
        model.addAttribute("vendedores", usuarioService.listarVendedores());

        return "vendedor/ventas"; // Ruta completa
    }

    @GetMapping("/cancelar") // Cancela la venta actual y limpia la sesion
    public String cancelarVenta(Model model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();

        model.addAttribute("cliente", new Cliente());
        model.addAttribute("producto", new Producto());
        model.addAttribute("detalleVenta", new ArrayList<>());
        model.addAttribute("total", 0.0);
        model.addAttribute("tipoComprobante", "");
        model.addAttribute("vendedores", usuarioService.listarVendedores());

        return "vendedor/ventas"; // Ruta completa
    }
}