package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.repository.IProductoRepository;

/**
 * Service implementation for product management.
 *
 * This class encapsulates the business logic related to products,
 * delegating persistence operations to {@link IProductoRepository}.
 */
@Service
public  class ProductoServiceImpl implements ProductoService {

	@Autowired
	private IProductoRepository productoRepository;

	/**
	 * Saves a product to the database.
	 * If the product already has an existing ID, it will be updated.
	 *
	 * @param producto entity to be saved
	 * @return persisted product
	 */
	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}

	/**
	 * Retrieves a product by its ID.
	 *
	 * @param id product identifier
	 * @return Optional containing the product if found
	 */
	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	/**
	 * Updates an existing product.
	 *
	 * Spring Data JPA automatically performs an update
	 * when the entity ID already exists.
	 *
	 * @param producto entity to be updated
	 */
	@Override
	public void update(Producto producto) {
		productoRepository.save(producto); // because it updates or save with null id

	}

	/**
	 * Deletes a product by its ID.
	 *
	 * @param id product identifier to delete
	 */
	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}

	/**
	 * Return all products from the database.
	 *
	 * @return list of products
	 */
	@Override
	 public List<Producto> findAll(){
		return productoRepository.findAll();
	}

	/**
	 * Finds products by name using case-insensitive partial matching.
	 *
	 * @param nombre text to search for
	 * @return list of matching products
	 */
	@Override
	public List<Producto> findByNombre(String nombre) {
		return productoRepository.findByNombreContainingIgnoreCase(nombre);
	}

}

