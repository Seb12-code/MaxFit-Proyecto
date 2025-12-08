package com.miempresa.proyectoFinal.Service;


import com.miempresa.proyectoFinal.Model.Producto;
import com.miempresa.proyectoFinal.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository){ this.productoRepository = productoRepository; }

    public void guardarProducto(Producto producto){ productoRepository.save(producto);}

    public List<Producto> listarProductos(){ return productoRepository.findAll();}

    public void eliminarProducto(Long id){ productoRepository.deleteById(id);}

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

}
