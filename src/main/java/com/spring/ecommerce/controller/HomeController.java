 package com.spring.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.service.IDetalleOrdenService;
import com.spring.ecommerce.service.IOrdenService;
import com.spring.ecommerce.service.IUsuarioService;
import com.spring.ecommerce.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>(); // to store details order

	Orden orden = new Orden(); // to save order an data about it

	@GetMapping("/") /// to show default view
	public String home(Model model, HttpSession session) {
		log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
		model.addAttribute("productos", productoService.findAll());
		model.addAttribute("sesion", session.getAttribute("idusuario")); // show user in navbar using session since
																			// model object
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parametro {}", id);
		Producto producto = new Producto();
		Optional<Producto> productoOpcional = productoService.get(id);
		producto = productoOpcional.get();
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}

	@PostMapping("/cesta")
	public String añadirCarito(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) { // @requestparam
																										// refrences to
																										// post
																										// parametres
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> productoOpcional = productoService.get(id); // get the product by id or optional null
		log.info("Producto añadido: {}", productoOpcional.get()); // return the producto object or exception if no exist
		log.info("Cantidad {}", cantidad);
		producto = productoOpcional.get(); // getter default to verify if product exist and generates it
											// A VER QUE PASA

		detalleOrden.setCantidad(cantidad); // set atributes to detalle orden
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validate to not repeat detail with same product
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto); // verify the same
																									// atributte in a
																									// object list
		if (!ingresado) { // if not the same add detalle to list

			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum(); // iINVESTIGATE LINE. Count an add a
																				// determinated atribute of list sheet
																				// details
		orden.setTotal(sumaTotal); //
		model.addAttribute("shopdetails", detalles);
		model.addAttribute("orden", orden);

		return "/usuario/cesta";
	}

	@GetMapping("delete/cesta/{id}")
	public String borrarProductoCesta(@PathVariable Integer id, Model model) { // delete 1 producto of cart, get the
																				// post params (Id only)
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>(); // new details sheet list after delete

		for (DetalleOrden detalleOrden : detalles) { // if each producto is not Post id add his detail in new list
			if (detalleOrden.getProducto().getId() != id) { // of course id 3 will not add it.
				ordenesNueva.add(detalleOrden);
			}
		}
		detalles = ordenesNueva; // update cart
		double sumaTotal = 0;

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum(); // sum all total of all details list

		orden.setTotal(sumaTotal); // set total in order to show in cart
		model.addAttribute("shopdetails", detalles);
		model.addAttribute("orden", orden);

		return "usuario/cesta";
	}

	@GetMapping("/getCesta") // show data about order and details before confirm shop
	public String getCesta(Model model, HttpSession session) { // get the order and details if it must to return cart

		model.addAttribute("shopdetails", detalles);
		model.addAttribute("orden", orden); // get detail an order generated in cesta
		model.addAttribute("sesion", session.getAttribute("idusuario")); // show user in navbar using session since
																			// model object
		return "/usuario/cesta";
	}

	@GetMapping("/verorden")
	public String verOrden(Model model, HttpSession session) {

		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		model.addAttribute("detalles", detalles);
		model.addAttribute("orden", orden); // get detail an order generated in cesta
		model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
	}

	@GetMapping("/guardarorden")
	public String guardarOrden(HttpSession session) { // not use model because will not o show in view but contains
														// values of
		// veOrden() method
		Date fechaCreacion = new Date(); // set date
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.darCodigoOrden()); // set code
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get(); // set
																															// Usuario
		orden.setUsuario(usuario);
		log.info("Ordenes guardada: {}", orden);
		ordenService.save(orden);

		for (DetalleOrden dt : detalles) { // assign details generated in order
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		orden = new Orden();
		detalles.clear();
		return "redirect:/"; // redirect to home
	}

	@PostMapping("/busqueda")
	public String buscarProducto(@RequestParam String nombre, Model model) {
		log.info("Nombre del producto:{}", nombre);
		List<Producto> productos = new ArrayList<Producto>();
		if (nombre != nombre.toLowerCase()) {
			productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre))
					.collect(Collectors.toList());
		} else {
			productos = productoService.findAll().stream().filter(p -> p.getNombre().toLowerCase().contains(nombre))
					.collect(Collectors.toList());
		}

		model.addAttribute("productos", productos);
		return "usuario/home";
	}

}
