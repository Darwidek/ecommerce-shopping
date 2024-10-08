package com.spring.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.model.Orden;

public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer>{

	
}
