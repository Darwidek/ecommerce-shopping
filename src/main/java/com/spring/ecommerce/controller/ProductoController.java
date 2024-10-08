package com.spring.ecommerce.controller;

import java.io.IOException;
import java.util.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.service.ProductoService;
import com.spring.ecommerce.service.UploadFileService;
import com.spring.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/productos") // it sets the url controller
public class ProductoController {
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class); // logger object to test controller

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	// @Autowired
	// private IUsuarioService usuarioService;

	@Autowired
	private UploadFileService ufs;

	@GetMapping("") // null because it shows a view
	public String show(Model model) { // it sets mainUrl/productos/show home model get backen information to view
		model.addAttribute("productos", productoService.findAll()); // MODEL OBJECT equals to java request object
		return "productos/show";
	}

	@GetMapping("/create") // it sets mainUrl/productos/create show home
	public String create() {
		return "productos/create"; // returns string and equals to request dispatcher with @getmapping anotation
	}

	@PostMapping("/save")
	public String guardarProducto(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException { // get the post
		// methods since
		// the form
		LOGGER.info("Se ejecuta el metodo de guardado(){}");
		Usuario user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		LOGGER.info("Datos de usuario creador {}:", user);
		producto.setUsuario(user);

		// save images
		if (producto.getId() == null) { // verify the product exist in http post
			String nombreImg = ufs.saveImage(file); // save file and generate his string name
			producto.setImagen(nombreImg); // add image in product
		} else {
		}
		productoService.save(producto);
		return "redirect:/productos";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id); // optional type if producto doesn´t exists
		producto = optionalProducto.get(); // if the value exists it generates that value
		LOGGER.info("Producto buscado; {}", producto);
		model.addAttribute("producto", producto);
		return "productos/edit";
	}

	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagem
			producto.setImagen(p.getImagen());
		} else {// cuando se edita tbn la imagen
				// eliminar cuando no sea la imagen por defecto

			if (!p.getImagen().equals("default.jpg")) {
				ufs.deleteImage(p.getImagen());
			}
			String nombreImagen = ufs.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {

		Producto p = new Producto();
		p = productoService.get(id).get();

		// delete if contains default image
		if (!p.getImagen().equals("default.jpg")) {
			ufs.deleteImage(p.getImagen());
		}

		productoService.delete(id);
		return "redirect:/productos";

	}

}
