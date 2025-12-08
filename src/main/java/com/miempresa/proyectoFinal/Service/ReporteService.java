package com.miempresa.proyectoFinal.Service;

import com.miempresa.proyectoFinal.Model.Venta;
import com.miempresa.proyectoFinal.Repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import com.miempresa.proyectoFinal.Model.Venta;
import com.miempresa.proyectoFinal.Repository.VentaRepository; // Asegúrate que esta importación sea correcta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional; // Importar Optional

@Service
public class ReporteService {

    @Autowired
    private VentaRepository ventaRepository;

    // Reporte general de ventas entre fechas
    // Obtiene todas las ventas dentro de un rango de fechas.
    public List<Venta> obtenerVentasEntreFechas(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            return ventaRepository.findByFechaBetween(desde, hasta);
        }
        return Collections.emptyList();
    }

    // Productos vendidos por rango de fechas
    // Obtiene el resumen de productos vendidos (nombre, cantidad, subtotal) en un rango.
    public List<Object[]> obtenerProductosVendidos(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            return ventaRepository.reporteProductosVendidos(desde, hasta);
        }
        return Collections.emptyList();
    }

    // Mejor venta del día
    // Encuentra la venta con el total más alto para una fecha específica.
    public Venta obtenerMejorVentaDelDia(LocalDate fecha) {
        if (fecha == null) {
            return null;
        }
        // Llamar al nuevo metodo en VentaRepository que ya hace el trabajo
        return ventaRepository.mejorVentaDelDia(fecha);
    }

    // Menor venta del día
    //  Encuentra la venta con el total más bajo para una fecha específica.
    public Venta obtenerMenorVentaDelDia(LocalDate fecha) {
        if (fecha == null) {
            return null;
        }
        // Llamar al nuevo metodo en VentaRepository que ya hace el trabajo
        return ventaRepository.menorVentaDelDia(fecha);
    }

    // Mejor vendedor (por total vendido en un rango de fechas)
    //  Obtiene los datos del vendedor con el mayor total de ventas en un rango.
    public Object[] obtenerMejorVendedor(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            return ventaRepository.mejorVendedor(desde, hasta);
        }
        return null; // Retorna null si no hay fechas
    }

    // Peor vendedor (por total vendido en un rango de fechas)
    // Obtiene los datos del vendedor con el menor total de ventas en un rango.
    public Object[] obtenerPeorVendedor(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            return ventaRepository.peorVendedor(desde, hasta);
        }
        return null; // Retorna null si no hay fechas
    }


    // Calcula el total de ventas por cada vendedor dentro de un rango de fechas.
    public List<Object[]> obtenerTotalesPorVendedor(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null) {
            return ventaRepository.obtenerTotalesPorVendedor(desde, hasta);
        }
        return Collections.emptyList();
    }
}