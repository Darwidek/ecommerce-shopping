package com.spring.ecommerce.service;

import com.spring.ecommerce.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.repository.IDetalleOrdenRepository;

import java.util.List;
import java.util.Optional;

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
	 * <p>
	 * If the entity already exists, it will be updated according to JPA behavior.
	 *
	 * @param detalleOrden order detail entity to save
	 * @return persisted order detail
	 */
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		return detalleOrdenRepository.save(detalleOrden);
	}


	@Override
	public void delete(Integer id) {
		detalleOrdenRepository.deleteById(id);
	}

	public List<DetalleOrden> obtenerCestaUsuario(Usuario usuario){
		return detalleOrdenRepository.findByUsuarioAndOrdenIsNull(usuario);
	}

	public Optional<DetalleOrden> obtenerPorIdYUsuario(Integer id, Usuario usuario){
		return detalleOrdenRepository.findByIdAndUsuario(id, usuario);
	}
}

