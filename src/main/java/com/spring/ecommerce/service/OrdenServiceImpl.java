package com.spring.ecommerce.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.DetalleOrden;
import jakarta.transaction.Transactional;
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
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	@Autowired
	private ProductoService productoService;


	/**
	 * Saves an order in the database.
	 */
	@Transactional
	@Override
	public void guardarCompra(Usuario usuario) {

		// 1. Obtener carrito
		List<DetalleOrden> detalles =
				detalleOrdenService.obtenerCestaUsuario(usuario);

		if (detalles == null || detalles.isEmpty()) {
			throw new RuntimeException("El carrito está vacío");
		}

		// 2. Calcular total de forma segura
		double total = detalles.stream()
				.mapToDouble(DetalleOrden::getTotal)
				.sum();

		// 3. Crear orden
		Orden orden = new Orden();
		orden.setFechaCreacion(new Date());
		orden.setNumero(darCodigoOrden());
		orden.setUsuario(usuario);
		orden.setTotal(total);

		ordenRepository.save(orden);

		// 4. Procesar detalles
		for (DetalleOrden detalle : detalles) {

			// vincular orden
			detalle.setOrden(orden);

			// (OPCIONAL PRO) validar stock
			if (detalle.getProducto().getCantidad() < detalle.getCantidad()) {
				throw new RuntimeException(
						"Stock insuficiente para " +
								detalle.getProducto().getNombre()
				);
			}

			// descontar stock
			detalle.getProducto().setCantidad(
                    (int) (detalle.getProducto().getCantidad() - detalle.getCantidad())
            );

			productoService.save(detalle.getProducto());

			// guardar detalle
			detalleOrdenService.save(detalle);
		}

		// Si algo falla arriba → ROLLBACK automático
	}

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

