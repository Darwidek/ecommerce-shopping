package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.repository.IProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private IProductoRepository productoRepository;

	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		productoRepository.save(producto); // because it updates or save with null id

	}

	@Override
	public void delete(Integer id) {
		productoRepository.deleteById(id);
	}
	
	 public List<Producto> findAll(){
		return productoRepository.findAll();
	}

}
