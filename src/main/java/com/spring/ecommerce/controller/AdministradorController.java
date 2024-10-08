package com.spring.ecommerce.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.service.IUsuarioService;
import com.spring.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

	private Logger log = LoggerFactory.getLogger(AdministradorController.class);

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("")
	public String home(Model model) { // it sets mainUrl/administrador/view home

		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);

		return "administrador/home";
	}

	@GetMapping("/usuarios")
	public String usuarios(Model model) {

		model.addAttribute("usuarios", usuarioService.findAll());
		 log.info("Redirijiendo a lista de usuarios");

		return "administrador/usuarios";
	}

}
