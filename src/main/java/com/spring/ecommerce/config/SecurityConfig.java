package com.spring.ecommerce.config;

import com.spring.ecommerce.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.spring.ecommerce.security.CustomAuthenticationSuccessHandler;


/**
 * Main Spring Security configuration class.
 *
 * <p>This class defines the security rules of the application, including:
 * authentication provider, access control rules, login/logout configuration,
 * and password encoding strategy.</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Handler executed after a successful authentication.
     * Used to redirect users based on their roles.
     */
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    /**
     * Defines the authentication provider used by Spring Security.
     *
     * <p>It delegates user lookup to {@link CustomUserDetailsService}
     * and uses a password encoder to validate credentials.</p>
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    /**
     * Configures the security filter chain (HTTP security rules).
     *
     * <p>Defines which endpoints are public, which require authentication,
     * and which require specific roles. Also configures login, logout,
     * and access denied handling.</p>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationProvider authenticationProvider) throws Exception {

        http
                .authenticationProvider(authenticationProvider)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/image/**", "/vendor/**").permitAll()
                        .requestMatchers("/usuario/login", "/usuario/registro", "/usuario/acceso","/usuario/save").permitAll()
                        .requestMatchers("/admin/**", "/productos/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/usuario/login")
                        .loginProcessingUrl("/usuario/acceso")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureUrl("/usuario/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/usuario/logout")
                        .logoutSuccessUrl("/usuario/login?logout=true")
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/denegado")
                );


        return http.build();
    }

    /**
     * Password encoder used to hash and verify user passwords.
     *
     * <p>BCrypt is used as it is a strong and widely recommended hashing algorithm
     * for production systems.</p>
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}