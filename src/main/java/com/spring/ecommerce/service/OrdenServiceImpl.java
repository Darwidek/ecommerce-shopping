package com.spring.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}

	@Override
	public String darCodigoOrden() { // to generate order code starting by 0000010

		int numero = 0;
		String numeroConcatenado = "";

		List<Orden> ordenes = findAll(); // get list order
		List<Integer> numeros = new ArrayList<Integer>(); // get integer list to add number orders

		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero()))); // for each order generates order
																						// number to int list
		if (ordenes.isEmpty()) { // if no order first code is 1
			numero = 1;
		} else {
			numero = numeros.stream().max(Integer::compare).get(); // get the max number of int list
			numero++; // and increment it to create next order and get the last order code
		}

		if (numero < 10) { // 00000000010
			numeroConcatenado = "0000000000" + String.valueOf(numero); // concate number list with 0 to build the code
		} else if (numero < 100) { // 000000000100
			numeroConcatenado = "000000000" + String.valueOf(numero);
		} else if (numero < 1000) { // 000000001000
			numeroConcatenado = "00000000" + String.valueOf(numero);
		} else if (numero < 10000) { // 000000010000
			numeroConcatenado = "0000000" + String.valueOf(numero);
		}
		return numeroConcatenado; // return code
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {

		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}

}
