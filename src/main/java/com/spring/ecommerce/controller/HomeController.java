package com.spring.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spring.ecommerce.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

/**
 * Main controller for the public shop.
 * <p>
 * Handles product browsing, shopping cart, order creation
 * and checkout process.
 */
@Controller
@RequestMapping("/") // url: http://localhost:8080/
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

    // Temporary in-memory cart (session-like behavior)
    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    Orden orden = new Orden(); // to save order an data about it

    /**
     * Home page with product listing.
     */
    @GetMapping("/")
    /// to show default view http://localhost:8080/
    public String home(Model model) {
        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);
        // model.addAttribute("username", user.getNickname());

        return "usuario/home";
    }

    /**
     * Product detail page.
     */
    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {
        log.info("Id producto enviado como parametro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOpcional = productoService.get(id);
        producto = productoOpcional.get();
        model.addAttribute("producto", producto);
        return "usuario/productohome";
    }

    /**
     * Adds a product to the shopping cart.
     */
    @PostMapping("/cesta")
    public String añadirCarito(@RequestParam Integer id, @RequestParam Integer cantidad, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Producto producto = productoService.get(id).get();
        DetalleOrden detalleOrden = new DetalleOrden();
        Usuario usuarioComprador = userDetails.getUsuario();
        DetalleOrden detalle = new DetalleOrden();


        detalleOrden.setCantidad(cantidad); // set atributes to detalle orden
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);
        detalleOrden.setUsuario(usuarioComprador);
        detalleOrden.setOrden(null);

        detalleOrdenService.save(detalleOrden);

        List<DetalleOrden> detalles =
                detalleOrdenService.obtenerCestaUsuario(usuarioComprador);

        double total = detalles.stream()
                .mapToDouble(DetalleOrden::getTotal)
                .sum();

        Orden orden = new Orden();
        orden.setTotal(total);

        model.addAttribute("details", detalles);
        model.addAttribute("detail", orden);

        return "usuario/cesta";
    }

    /**
     * Removes a product from the shopping cart.
     */
    @GetMapping("/delete/cesta/{id}")
    public String borrarProductoCesta(@PathVariable Integer id,
                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                      Model model) {

        Usuario usuario = userDetails.getUsuario();

        Optional<DetalleOrden> detalle =
                detalleOrdenService.obtenerPorIdYUsuario(id, usuario);

        if (detalle.isPresent()) {
            detalleOrdenService.delete(detalle.get().getId());
        }

        List<DetalleOrden> detalles =
                detalleOrdenService.obtenerCestaUsuario(usuario);

        double total = detalles.stream()
                .mapToDouble(DetalleOrden::getTotal)
                .sum();

        Orden orden = new Orden();
        orden.setTotal(total);

        model.addAttribute("details", detalles);
        model.addAttribute("detail", orden);

        return "usuario/cesta";
    }

    /**
     * Displays current cart.
     */
    @GetMapping("/getCesta") // show data about order and details before confirm shop
    public String getCesta(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) { // get the order and details if it must to return cart


        Usuario usuarioComprador = userDetails.getUsuario();
        List<DetalleOrden> detalles =
                detalleOrdenService.obtenerCestaUsuario(usuarioComprador);

        double total = detalles.stream()
                .mapToDouble(DetalleOrden::getTotal)
                .sum();

        Orden orden = new Orden();
        orden.setTotal(total);

        model.addAttribute("details", detalles);
        model.addAttribute("detail", orden);

        return "usuario/cesta";
    }

    /**
     * Order summary before checkout.
     */
    @GetMapping("/verorden")
    public String verOrden(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Usuario usuarioComprador = userDetails.getUsuario();
        List<DetalleOrden> detalles =
                detalleOrdenService.obtenerCestaUsuario(usuarioComprador);
        double total = detalles.stream()
                .mapToDouble(DetalleOrden::getTotal)
                .sum();

        Orden orden = new Orden();
        orden.setTotal(total);

        model.addAttribute("details", detalles);
        model.addAttribute("detail", orden); // get detail an order generated in cesta
        model.addAttribute("usuario", usuarioComprador);
        return "usuario/resumenorden";
    }

    /**
     * Finalizes and saves the order.
     */

/*
@GetMapping("/guardarorden")
	public String guardarOrden(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Date fechaCreacion = new Date(); // set date
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.darCodigoOrden());
		Usuario usuarioComprador = userDetails.getUsuario();
		List<DetalleOrden> detalles =
				detalleOrdenService.obtenerCestaUsuario(usuarioComprador);
		double total = detalles.stream()
				.mapToDouble(DetalleOrden::getTotal)
				.sum();
																															// Usuario
		orden.setUsuario(usuarioComprador);
		orden.setTotal(total);
		ordenService.save(orden);

		for (DetalleOrden dt : detalles) { // assign details generated in order
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		orden = new Orden();
		detalles.clear();
		return "redirect:/"; // redirect to home
	}
 */
    @GetMapping("/guardarorden")
    public String guardarOrden(@AuthenticationPrincipal CustomUserDetails userDetails) {

        ordenService.guardarCompra(userDetails.getUsuario());

        return "redirect:/";
    }

    /**
     * Product search by name.
     */
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
