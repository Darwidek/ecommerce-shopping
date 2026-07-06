package com.spring.ecommerce.repository;

import com.spring.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.model.Orden;

import java.util.List;
import java.util.Optional;

public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer>{

    List<DetalleOrden> findByUsuarioAndOrdenIsNull(Usuario usuario);
    Optional<DetalleOrden> findByIdAndUsuario(Integer id, Usuario usuario);
}
