package com.spring.ecommerce.controller;

import java.io.IOException;
import java.util.*;

import com.spring.ecommerce.security.CustomUserDetails;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller responsible for managing product-related web operations.
 *
 * Provides endpoints for listing, creating, editing, updating,
 * deleting and searching products.
 *
 * Each product is associated with an authenticated user.
 */
@Controller
@RequestMapping("/productos") // it sets the url controller
public class ProductoController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class); // logger object to test controller

    @Autowired
    private ProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;


    @Autowired
    private UploadFileService ufs;

    /**
     * Displays the list of all products.
     *
     * @param model Spring Model to send data to the view
     * @return view name "productos/show"
     */
    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    /**
     * Shows the product creation form.
     *
     * @return view name "productos/create"
     */
    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    /**
     * Saves a new product associated with the authenticated user.
     *
     * @param producto product data from form
     * @param file image uploaded for the product
     * @param userDetails authenticated user from Spring Security
     * @return redirect to product list
     * @throws IOException if image upload fails
     */
    @PostMapping("/save")
    public String guardarProducto(Producto producto, @RequestParam("img") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails)
            throws IOException {
        Usuario user = userDetails.getUsuario();
        producto.setUsuario(user);

        if (producto.getId() == null) {
            String nombreImg = ufs.saveImage(file);
            producto.setImagen(nombreImg);
        } else {
        }
        productoService.save(producto);
        return "redirect:/productos";
    }

    /**
     * Loads product data into the edit form.
     *
     * @param id product identifier
     * @param model Spring Model
     * @return view name "productos/edit"
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id); // optional type if producto doesn´t exists
        producto = optionalProducto.get(); // if the value exists it generates that value
        LOGGER.info("Producto buscado; {}", producto);
        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    /**
     * Updates an existing product, including optional image change.
     *
     * @param producto updated product data
     * @param file new image file (optional)
     * @return redirect to product list
     * @throws IOException if image processing fails
     */
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

    /**
     * Deletes a product by its ID and removes its image if necessary.
     *
     * @param id product identifier
     * @return redirect to product list
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Producto p = productoService.get(id).get();

        try {
            if (!p.getImagen().equals("default.jpg")) {
                ufs.deleteImage(p.getImagen());
            }
        } catch (IOException e) {
            e.printStackTrace(); // mejor usar log.error en producción
        }

        productoService.delete(id);

        return "redirect:/productos";
    }

    /**
     * Searches products by name.
     *
     * @param nombre search term
     * @param model Spring Model
     * @return view name "productos/show"
     */
    @GetMapping("/search")
    public String search(@RequestParam("nombre") String nombre, Model model) {
        LOGGER.info("Buscando productos con nombre: {}", nombre);

        List<Producto> productos = productoService.findByNombre(nombre);

        model.addAttribute("productos", productos);
        model.addAttribute("nombre", nombre); // opcional, para mantener lo buscado en la vista

        return "productos/show";
    }

}
