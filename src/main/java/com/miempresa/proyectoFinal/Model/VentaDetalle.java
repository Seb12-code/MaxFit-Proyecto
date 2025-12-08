package com.miempresa.proyectoFinal.Model;

import jakarta.persistence.*;

@Entity
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Producto
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Relación con Venta
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double subtotal;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}

