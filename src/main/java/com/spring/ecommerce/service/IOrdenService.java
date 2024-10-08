package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Usuario;

public interface IOrdenService {

	List<Orden> findAll();

	Orden save(Orden orden);

	String darCodigoOrden();

	List<Orden> findByUsuario(Usuario usuario);

	Optional<Orden> findById(Integer id);
}
