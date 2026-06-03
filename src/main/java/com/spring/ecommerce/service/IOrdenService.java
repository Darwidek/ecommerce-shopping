package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Usuario;

/**
 * Service contract for managing orders.
 *
 * Defines the business operations related to order processing,
 * including creation, retrieval, and order code generation.
 *
 * This abstraction allows different implementations and keeps
 * business logic separated from persistence concerns.
 */
public interface IOrdenService {

	/**
	 * Retrieves all orders from the system.
	 *
	 * @return list of all orders
	 */
	List<Orden> findAll();

	/**
	 * Persists an order in the database.
	 *
	 * @param orden order entity to be saved
	 * @return persisted order entity
	 */
	Orden save(Orden orden);

	/**
	 * Generates a unique sequential order code.
	 *
	 * @return generated order code
	 */
	String darCodigoOrden();

	/**
	 * Retrieves all orders associated with a specific user.
	 *
	 * @param usuario user whose orders are to be retrieved
	 * @return list of orders belonging to the given user
	 */
	List<Orden> findByUsuario(Usuario usuario);

	/**
	 * Retrieves an order by its unique identifier.
	 *
	 * @param id order identifier
	 * @return Optional containing the order if found
	 */
	Optional<Orden> findById(Integer id);
}
