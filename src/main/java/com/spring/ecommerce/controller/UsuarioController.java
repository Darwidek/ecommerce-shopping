package com.spring.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.http11.Http11InputBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.repository.IUsuarioRepository;
import com.spring.ecommerce.service.IOrdenService;
import com.spring.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario) { // if the name of post form is same Model object is not necessary
		logger.info("Usuario registro: {}", usuario);
		String passwordCodificada = passwordEncoder.encode(usuario.getPassword());
		usuario.setTipo("USER");
		usuario.setPassword(passwordCodificada);
		logger.info("Usuario registro: {}", usuario);
		usuarioService.save(usuario);

		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@PostMapping("/acceso") // if the name of post form is same Model object is not necessary
	public String autenticador(Usuario usuario, HttpSession session) { // httpsession object persist for all spring
																		// application
		logger.info("Accesos : {}", usuario);

		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());
		// logger.info("Usuario de db: {}", user.get());
		if (user.isPresent()) { // verify if exist optional generated
			session.setAttribute("idusuario", user.get().getId()); // ad user in a session object
			logger.info("Usuario de db: {}", user.get());
			if (user.get().getTipo().equals("ADMIN")) { // verify his type to redirect
				return "redirect:/administrador"; // redirect admin view in case admin
			} else {
				return "redirect:/"; // redirect default view in case user
			}
		} else {
			logger.info("El usuario no existe en el acceso");
		}

		return "redirect:/";
	}

	@GetMapping("/compras")
	public String usuarioCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario")); // grab id user in model to navbar
		// find by id user in session
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		List<Orden> ordenes = ordenService.findByUsuario(usuario); // filter orders in list by user
		model.addAttribute("ordenes", ordenes); // get list of orders filtered in model to show in view

		return "usuario/compras";
	}

	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession sesssion, Model model) {
		logger.info("Id de la orden: {}", id);
		Optional<Orden> orden = ordenService.findById(id); // find order by id
		model.addAttribute("detalles", orden.get().getDetalle()); // get details of order and trnsfar by model

		model.addAttribute("sesion", sesssion.getAttribute("idusuario")); // transfer by model object id user
		return "usuario/detallecompra";
	}

	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}

	@GetMapping("/fijar/{id}")
	public String fijarFavorita(@PathVariable Integer id) {
		Orden orden = ordenService.findById(id).get(); // find order by id
		logger.info("Encuentra la orden: {}" + orden); // verifies it
		byte numFijado = 1;
		orden.setEsFavorita(numFijado);
		logger.info("Verificando dato fijado: " + orden.getEsFavorita());
		ordenService.save(orden);
		return "redirect:/usuario/compras";
	}

	@GetMapping("/desfijar/{id}")
	public String desfijarFavorita(@PathVariable Integer id) {
		Orden orden = ordenService.findById(id).get(); // find order by id
		logger.info("Encuentra la orden: {}" + orden); // verifies it
		byte numFijado = 0;
		orden.setEsFavorita(numFijado);
		logger.info("Verificando dato fijado: " + orden.getEsFavorita());
		ordenService.save(orden);
		return "redirect:/usuario/compras";
	}

}
