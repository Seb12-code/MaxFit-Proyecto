package com.miempresa.proyectoFinal.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private String tipoComprobante;

    private double subtotal;

    private double igv;

    private double total;

    private String numeroComprobante;

    // Relación con Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id") //  columna FK
    private Cliente cliente;

    // Relación con Vendedor
    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;

    // Relación con los detalles de venta
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true) // Añadido orphanRemoval para limpieza de detalles
    private List<VentaDetalle> detalles;



    //  Constructor
    public Venta() {}

    // campos principales
    public Venta(LocalDate fecha, String tipoComprobante, double subtotal, double igv, double total, String numeroComprobante, Cliente cliente, Usuario vendedor) {
        this.fecha = fecha;
        this.tipoComprobante = tipoComprobante;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
        this.numeroComprobante = numeroComprobante;
        this.cliente = cliente;
        this.vendedor = vendedor;
    }


    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public List<VentaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
}
