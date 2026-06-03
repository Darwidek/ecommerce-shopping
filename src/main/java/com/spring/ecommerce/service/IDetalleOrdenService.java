package com.spring.ecommerce.service;

import com.spring.ecommerce.model.DetalleOrden;

/**
 * Service contract for managing order details.
 *
 * Defines the business operations related to order line items,
 * such as persisting order detail information in the database.
 *
 * This abstraction allows multiple implementations if needed
 * (e.g., different persistence strategies or business rules).
 */
public interface IDetalleOrdenService {

	/**
	 * Persists an order detail entity.
	 *
	 * @param detalleOrden order detail entity to be saved
	 * @return persisted order detail entity
	 */
	DetalleOrden save(DetalleOrden detalleOrden);
}
