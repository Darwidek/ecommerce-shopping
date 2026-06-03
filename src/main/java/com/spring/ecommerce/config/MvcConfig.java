package com.spring.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * MVC configuration class used to define custom web-related settings.
 *
 * <p>This configuration is responsible for mapping static resources
 * that are stored outside the application classpath.</p>
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {


    /**
     * Configures the location of uploaded images so they can be
     * accessed via HTTP requests.
     *
     * <p>Maps the URL pattern {@code /image/**} to the local filesystem
     * directory {@code uploads/image/}.</p>
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:uploads/image/");
    }
}