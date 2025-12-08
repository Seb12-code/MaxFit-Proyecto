package com.miempresa.proyectoFinal.Repository;

import com.miempresa.proyectoFinal.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    //  Metodos ajustados para incluir filtros de fecha

    // Productos vendidos (por nombre de producto) en un rango de fechas
    // Obtiene el nombre del producto, la suma de cantidades vendidas y el subtotal de cada producto dentro de un rango de fechas.
    @Query("SELECT vd.producto.nombre, SUM(vd.cantidad), SUM(vd.subtotal) " +
            "FROM VentaDetalle vd JOIN vd.venta v " +
            "WHERE v.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY vd.producto.nombre")
    List<Object[]> reporteProductosVendidos(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta); //  Parámetros de fecha

    // Mejor venta del dia
    // Busca la venta con el total más alto para una fecha específica.
    @Query(value = "SELECT * FROM venta WHERE fecha = :fecha ORDER BY total DESC LIMIT 1", nativeQuery = true)
    Venta mejorVentaDelDia(@Param("fecha") LocalDate fecha);

    // Menor venta del dia
    //  Busca la venta con el total más bajo para una fecha específica.
    @Query(value = "SELECT * FROM venta WHERE fecha = :fecha ORDER BY total ASC LIMIT 1", nativeQuery = true)
    Venta menorVentaDelDia(@Param("fecha") LocalDate fecha);

    // Ventas entre fechas - Correcto y se mantiene
    // Obtiene todas las ventas realizadas dentro de un rango de fechas.
    List<Venta> findByFechaBetween(LocalDate desde, LocalDate hasta);

    // Mejor vendedor (mayor total vendido) en un rango de fechas
    // Encuentra al vendedor con el total de ventas más alto dentro de un período específico.
    @Query("SELECT v.vendedor.id, v.vendedor.nombre, SUM(v.total) AS totalVendido " +
            "FROM Venta v " +
            "WHERE v.fecha BETWEEN :desde AND :hasta " + //
            "GROUP BY v.vendedor.id, v.vendedor.nombre " +
            "ORDER BY totalVendido DESC")
    List<Object[]> findTopVendedores(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta, org.springframework.data.domain.Pageable pageable);

    // Metodo default para mantener la interfaz de mejorVendedor
    //  Wrapper para obtener al mejor vendedor usando el metodo findTopVendedores con paginación.
    default Object[] mejorVendedor(LocalDate desde, LocalDate hasta) { // Parametros de fecha
        return findTopVendedores(desde, hasta, org.springframework.data.domain.PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
    }

    // Peor vendedor (menor total vendido) en un rango de fechas
    // Encuentra al vendedor con el total de ventas más bajo dentro de un período específico.
    @Query("SELECT v.vendedor.id, v.vendedor.nombre, SUM(v.total) AS totalVendido " +
            "FROM Venta v " +
            "WHERE v.fecha BETWEEN :desde AND :hasta " + //
            "GROUP BY v.vendedor.id, v.vendedor.nombre " +
            "ORDER BY totalVendido ASC")
    List<Object[]> findBottomVendedores(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta, org.springframework.data.domain.Pageable pageable);

    // Metodo default para mantener la interfaz de peorVendedor
    //  Wrapper para obtener al peor vendedor usando el metodo findBottomVendedores con paginación.
    default Object[] peorVendedor(LocalDate desde, LocalDate hasta) { // Parámetros de fecha
        return findBottomVendedores(desde, hasta, org.springframework.data.domain.PageRequest.of(0, 1))
                .stream().findFirst().orElse(null);
    }

    // Totales por vendedor en un rango de fechas
    //  Calcula el total de ventas por cada vendedor dentro de un rango de fechas.
    @Query("SELECT v.vendedor.nombre, SUM(v.total) " +
            "FROM Venta v " +
            "WHERE v.fecha BETWEEN :desde AND :hasta " +
            "GROUP BY v.vendedor.nombre")
    List<Object[]> obtenerTotalesPorVendedor(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta); //  Parámetros de fecha
}