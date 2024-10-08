package com.spring.ecommerce;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Desactivar CSRF para simplificar el ejemplo
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll() // Permitir el acceso a todas las rutas sin
																				// autenticación
				);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(IUsuarioService usuarioService) {
		return email -> {
			// Buscar usuario por email en el servicio
			Usuario usuario = usuarioService.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

			// Asignar los roles correspondientes según el tipo de usuario
			List<SimpleGrantedAuthority> authorities = usuario.getTipo().equals("ADMIN")
					? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
					: List.of(new SimpleGrantedAuthority("ROLE_USER"));

			// Devolver un objeto User de Spring Security
			return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getPassword(),
					authorities);
		};
	}

}
