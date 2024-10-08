package com.spring.ecommerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Mapear la carpeta de imágenes fuera de "static" a una ruta accesible por la
		// URL
		registry.addResourceHandler("/image/**").addResourceLocations("file:./image/"); // Ajusta la ruta según dónde
																						// estén tus imágenes
	}
}
