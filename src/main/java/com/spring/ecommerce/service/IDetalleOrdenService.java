package com.spring.ecommerce.service;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Service contract for managing order details.
 * <p>
 * Defines the business operations related to order line items,
 * such as persisting order detail information in the database.
 * <p>
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

    public void delete(Integer id);

    List<DetalleOrden> obtenerCestaUsuario(Usuario usuario);

    Optional<DetalleOrden> obtenerPorIdYUsuario(Integer id, Usuario usuario);
}
