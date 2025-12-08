package com.miempresa.proyectoFinal.Model;

import java.time.LocalDate;

public class ReporteDTO {
    private LocalDate fechaVenta;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double total;
    private String nombreVendedor;

    // Constructor
    public ReporteDTO(LocalDate fechaVenta, String producto, int cantidad, double precioUnitario, double total, String nombreVendedor) {
        this.fechaVenta = fechaVenta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.nombreVendedor = nombreVendedor;
    }

    // Getters y Setters
    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getNombreVendedor() { return nombreVendedor; }
    public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }
}
