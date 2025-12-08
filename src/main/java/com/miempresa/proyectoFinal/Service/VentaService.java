package com.miempresa.proyectoFinal.Service;

import com.miempresa.proyectoFinal.Model.*;
import com.miempresa.proyectoFinal.Repository.ClienteRepository;
import com.miempresa.proyectoFinal.Repository.ProductoRepository;
import com.miempresa.proyectoFinal.Repository.VentaDetalleRepository;
import com.miempresa.proyectoFinal.Repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.*;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaDetalleRepository detalleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Cliente buscarClientePorDni(String dni) {
        return clienteRepository.findByDni(dni).orElse(null);
    }

    public Producto buscarProductoPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream().findFirst().orElse(null);
    }

    public List<Venta> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) {
        return ventaRepository.findByFechaBetween(desde, hasta);
    }



    public Venta guardarVenta(Venta venta) {
        // Establecer la relacion bidireccional venta <-> detalles
        for (VentaDetalle detalle : venta.getDetalles()) {
            detalle.setVenta(venta);
        }

        // Guardar la venta y genera id
        Venta ventaGuardada = ventaRepository.save(venta);

        // Guardar los detalles y actualizar stock de productos
        for (VentaDetalle detalle : venta.getDetalles()) {
            // Descontar stock
            Producto producto = detalle.getProducto();
            int stockActual = producto.getStock();
            int cantidadVendida = detalle.getCantidad();
            producto.setStock(stockActual - cantidadVendida);

            // Guardar el nuevo stock en la BD
            productoRepository.save(producto);

            // Guardar el detalle
            detalleRepository.save(detalle);
        }

        return ventaGuardada;
    }

}