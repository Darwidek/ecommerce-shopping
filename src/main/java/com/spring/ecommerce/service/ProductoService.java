package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.Producto;

/**
 * Service contract for managing products.
 *
 * Defines the business operations related to product management,
 * including creation, retrieval, updating, deletion, and search operations.
 *
 * This abstraction allows the business logic to remain decoupled
 * from the persistence layer.
 */
public interface ProductoService {

	/**
	 * Persists a product in the system.
	 *
	 * @param producto product entity to be saved
	 * @return persisted product entity
	 */
	public Producto save(Producto producto);

	/**
	 * Retrieves a product by its unique identifier.
	 *
	 * @param id product identifier
	 * @return Optional containing the product if found
	 */
	public Optional<Producto> get(Integer id);

	/**
	 * Updates an existing product.
	 *
	 * @param producto product entity with updated information
	 */
	public void update(Producto producto);

	/**
	 * Deletes a product by its identifier.
	 *
	 * @param id product identifier to delete
	 */
	public void delete(Integer id);

	/**
	 * Retrieves all products from the system.
	 *
	 * @return list of all products
	 */
	public List<Producto> findAll();

	/**
	 * Searches products by name using case-insensitive partial matching.
	 *
	 * @param nombre text to search for
	 * @return list of matching products
	 */
	List<Producto> findByNombre(String nombre);


}
