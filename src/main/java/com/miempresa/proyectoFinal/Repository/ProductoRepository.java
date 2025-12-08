package com.miempresa.proyectoFinal.Repository;

import com.miempresa.proyectoFinal.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo); // busca por codigo

    List<Producto> findByNombreContainingIgnoreCase(String nombre); // busqueda por nombre
}
