package com.spring.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.repository.IOrdenRepository;

/**
 * Service implementation for order management.
 * Contains business logic for order creation, retrieval and user filtering.
 */
@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	/**
	 * Saves an order in the database.
	 */
	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	/**
	 * Returns all orders in the system.
	 */
	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}

	/**
	 * Generates a sequential order code with zero padding.
	 * Format example: 0000000001, 0000000002, ...
	 */
	@Override
	public String darCodigoOrden() {

		int numero = 0;

		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<>();

		ordenes.stream()
				.map(Orden::getNumero)
				.filter(n -> n != null && !n.isBlank())
				.forEach(n -> numeros.add(Integer.parseInt(n)));

		if (ordenes.isEmpty() || numeros.isEmpty()) {
			numero = 1;
		} else {
			numero = numeros.stream().max(Integer::compareTo).orElse(0);
			numero++;
		}

		return String.format("%010d", numero);
	}

	/**
	 * Retrieves all orders for a given user.
	 */
	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}

	/**
	 * Finds an order by its ID.
	 */
	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}
}

