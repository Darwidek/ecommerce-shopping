package com.spring.ecommerce.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.security.CustomUserDetails;
import com.spring.ecommerce.service.ProductoService;
import org.apache.coyote.http11.Http11InputBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

// import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IOrdenService ordenService;



	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario) { // if the name of post form is same Model object is not necessary
		usuario.setTipo("USER");
		String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(passwordEncriptada); // encripting password
		usuarioService.save(usuario);

		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {

		logger.info("TIPO DE USUARIO:");
		return "usuario/login";
	}

	@GetMapping("/compras")
	public String usuarioCompras(Model model,@AuthenticationPrincipal CustomUserDetails userDetails) {

		Usuario usuario = userDetails.getUsuario();

		List<Orden> ordenes = ordenService.findByUsuario(usuario); // filter orders in list by user
		model.addAttribute("ordenes", ordenes); // get list of orders filtered in model to show in vie

		return "usuario/compras";
	}


	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, Model model) {
		Optional<Orden> orden = ordenService.findById(id); // find order by id
		model.addAttribute("detalles", orden.get().getDetalle()); // get details of order and trnsfar by model
		return "usuario/detallecompra";
	}

	@GetMapping("/logout")
	public String cerrarSesion() {
		return "redirect:/";
	}
	/*
	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}
	* */

	@GetMapping("/fijar/{id}")
	public String fijarFavorita(@PathVariable Integer id) {
		Orden orden = ordenService.findById(id).get(); // find order by id
		byte numFijado = 1;
		orden.setEsFavorita(numFijado);
		ordenService.save(orden);
		return "redirect:/usuario/compras";
	}

	@GetMapping("/desfijar/{id}")
	public String desfijarFavorita(@PathVariable Integer id) {
		Orden orden = ordenService.findById(id).get(); // find order by id
		byte numFijado = 0;
		orden.setEsFavorita(numFijado);
		ordenService.save(orden);
		return "redirect:/usuario/compras";
	}

	@GetMapping("/denegado")
	public String accesoDenegado() {
		return "denegado";
	}

}
