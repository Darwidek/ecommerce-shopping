package com.spring.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.repository.IDetalleOrdenRepository;

/**
 * Service implementation for managing order details.
 *
 * This service handles the persistence of order line items,
 * including quantity and price information, delegating database
 * operations to {@link IDetalleOrdenRepository}.
 */

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService {

	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;

	/**
	 * Persists an order detail in the database.
	 *
	 * If the entity already exists, it will be updated according to JPA behavior.
	 *
	 * @param detalleOrden order detail entity to save
	 * @return persisted order detail
	 */
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		return detalleOrdenRepository.save(detalleOrden );
	}

}

