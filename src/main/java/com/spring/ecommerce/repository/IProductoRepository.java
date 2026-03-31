package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.ecommerce.model.Producto;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> { // defines repository, table object an ID

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}
