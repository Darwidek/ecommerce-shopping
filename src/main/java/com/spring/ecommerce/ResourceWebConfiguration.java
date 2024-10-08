package com.spring.ecommerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // to manage static recources (CSS,JS,images etc) without the server
public class ResourceWebConfiguration implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) { // method to configure url standard image
		registry.addResourceHandler("/image/**").addResourceLocations("file:image/");
	}
}
