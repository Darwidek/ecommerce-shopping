package com.spring.ecommerce.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.security.CustomUserDetails;
import com.spring.ecommerce.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.service.IUsuarioService;
import com.spring.ecommerce.service.ProductoService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin controller for managing products and users.
 */
@Controller
@RequestMapping("/admin")
public class AdministradorController {

    private Logger log = LoggerFactory.getLogger(AdministradorController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UploadFileService ufs;

    /**
     * Admin dashboard with product list.
     */
    @GetMapping("")
    public String home(Model model) { // it sets main Url localhost:8080/admin/ view home

        List<Producto> productos = productoService.findAll();
        model.addAttribute("productos", productos);

        return "admin/home";
    }

    /**
     * User list page.
     */
    @GetMapping("/usuarios")
    public String usuarios(Model model) {

        model.addAttribute("usuarios", usuarioService.findAll());
        log.info("Redirijiendo a lista de usuarios");

        return "admin/usuarios";
    }

    /**
     * Product list view.
     */
    @GetMapping("/productos")
    public String show(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "admin/show";
    }

    /**
     * Product creation form.
     */
    @GetMapping("/productos/create")
    public String create() {
        return "admin/create";
    }

    /**
     * Saves or updates a product. Handles image upload.
     */
    @PostMapping("/productos/save")
    public String guardarProducto(Producto producto, @RequestParam("img") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails)
            throws IOException {

        Usuario user = userDetails.getUsuario();
        producto.setUsuario(user);

        // save images
        if (producto.getId() == null) {
            String nombreImg = ufs.saveImage(file);
            producto.setImagen(nombreImg);
        } else {
        }
        productoService.save(producto);
        return "redirect:/admin/productos";
    }

    /**
     * Edit product form.
     */
    @GetMapping("/productos/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        producto = optionalProducto.get();
        model.addAttribute("producto", producto);
        return "admin/edit";
    }

    /**
     * Updates product and replaces image if needed.
     */
    @PostMapping("/productos/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Producto p = new Producto();
        p = productoService.get(producto.getId()).get();
        if (file.isEmpty()) {
            producto.setImagen(p.getImagen());
        } else {

            if (!p.getImagen().equals("default.jpg")) {
                ufs.deleteImage(p.getImagen());
            }
            String nombreImagen = ufs.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/admin/productos";
    }

    /**
     * Deletes product and its image if not default.
     */
    @GetMapping("/productos/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Producto p = productoService.get(id).get();

        try {
            if (!p.getImagen().equals("default.jpg")) {
                ufs.deleteImage(p.getImagen());
            }
        } catch (IOException e) {
            e.printStackTrace(); // log.error in production
        }

        productoService.delete(id);

        return "redirect:/admin/productos";
    }

    /**
     * Search products by name.
     */
    @GetMapping("/search")
    public String search(@RequestParam("nombre") String nombre, Model model) {

        List<Producto> productos = productoService.findByNombre(nombre);

        model.addAttribute("productos", productos);
        model.addAttribute("nombre", nombre);

        return "productos/show";
    }

}
