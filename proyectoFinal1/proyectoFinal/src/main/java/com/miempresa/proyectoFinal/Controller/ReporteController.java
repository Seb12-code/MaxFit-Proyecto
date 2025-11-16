package com.miempresa.proyectoFinal.Controller;

import com.miempresa.proyectoFinal.Model.Venta;
import com.miempresa.proyectoFinal.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public String mostrarReportes(
            @RequestParam(name = "desde", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,

            @RequestParam(name = "hasta", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,

            Model model) {

        //  controlar si el modal debe mostrarse
        boolean mostrarModal = false;

        // Si se han proporcionado fechas en la URL se manda el formulario
        if (desde != null && hasta != null) {
            mostrarModal = true; // Activar la ventana para mostrar el modal
        } else {
            // Si no hay fechas en la URL, establecer las del dia actual como valores por defecto
            desde = LocalDate.now();
            hasta = LocalDate.now();
        }

        //  Reporte por el rango de fecha
        List<Object[]> productosVendidos = reporteService.obtenerProductosVendidos(desde, hasta);
        model.addAttribute("productosVendidos", productosVendidos != null ? productosVendidos : Collections.emptyList());

        Venta mejorVenta = reporteService.obtenerMejorVentaDelDia(desde);
        model.addAttribute("mejorVentaDelDia", mejorVenta);

        Venta menorVenta = reporteService.obtenerMenorVentaDelDia(desde);
        model.addAttribute("menorVentaDelDia", menorVenta);

        Object[] mejorVendedor = reporteService.obtenerMejorVendedor(desde, hasta);
        model.addAttribute("mejorVendedor", mejorVendedor);

        Object[] peorVendedor = reporteService.obtenerPeorVendedor(desde, hasta);
        model.addAttribute("peorVendedor", peorVendedor);

        // Reporte general entre fechas (para el modal)
        List<Venta> ventasEntreFechas = reporteService.obtenerVentasEntreFechas(desde, hasta);
        model.addAttribute("ventasGenerales", ventasEntreFechas != null ? ventasEntreFechas : Collections.emptyList());

        // Atributos de fecha para la vista
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("fechaSeleccionada", desde);

        // Añadir la ventana para controlar la visibilidad del modal en el frontend
        model.addAttribute("mostrarModal", mostrarModal);

        return "vendedor/reportes";
    }
}