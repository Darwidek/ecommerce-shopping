package com.spring.ecommerce.security;


import com.spring.ecommerce.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of {@link UserDetails} that adapts the domain entity
 * {@link Usuario} to Spring Security's authentication model.
 *
 * <p>This class acts as an adapter between the application's user entity
 * and Spring Security, allowing authentication and authorization to be
 * performed using database-stored users.</p>
 */
public class CustomUserDetails implements UserDetails {

    /**
     * The authenticated domain user associated with the security context.
     */
    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Returns the authorities (roles) granted to the user.
     *
     * <p>The user's {@code tipo} field is mapped to Spring Security roles
     * with the prefix {@code ROLE_}. If the value is null, empty, or invalid,
     * it defaults to {@code USER}.</p>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String tipo = usuario.getTipo();

        if (tipo == null || tipo.isBlank()) {
            tipo = "USER";
        }

        tipo = tipo.toUpperCase();

        if (!tipo.equals("ADMIN") && !tipo.equals("USER")) {
            tipo = "USER";
        }

        return List.of(
                new SimpleGrantedAuthority("ROLE_" + tipo)
        );
    }

    /**
     * Returns the encrypted password used for authentication.
     */
    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    /**
     * Returns the username used for login authentication.
     *
     * <p>In this application, the username field is used as the login identifier.</p>
     */
    @Override
    public String getUsername() {
        return usuario.getUsername(); // Usamos email como login
    }

    /**
     * Returns a human-readable identifier for UI display purposes.
     */
    public String getDisplayName() {
        return usuario.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Returns the underlying domain {@link Usuario} entity.
     *
     * <p>This allows access to full user information within controllers
     * via {@code @AuthenticationPrincipal}.</p>
     */
    public Usuario getUsuario() {
        return usuario;
    }
}