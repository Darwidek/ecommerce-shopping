package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.repository.IUsuarioRepository;

/**
 * Service implementation for user management.
 *
 * This class handles business operations related to users,
 * delegating persistence operations to {@link IUsuarioRepository}.
 */
@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id user identifier
     * @return Optional containing the user if found
     */
	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

    /**
     * Persists a user in the database.
     *
     * @param usuario user entity to be saved
     * @return persisted user entity
     */
	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

    /**
     * Retrieves a user by email address.
     *
     * @param email user email
     * @return Optional containing the user if found
     */
	@Override
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

    /**
     * Retrieves all users from the system.
     *
     * @return list of all users
     */
	@Override
	public List<Usuario> findAll() {

		return usuarioRepository.findAll();
	}

    /**
     * Registers a new user in the system.
     *
     * This method may include business logic such as validation,
     * password encryption, and role assignment before persistence.
     *
     * @param usuario user entity to register
     * @return registered user entity
     */
	 @Override
	    public Usuario registrarUsuario(Usuario usuario) {
	        // Encripta la contraseña antes de guardarla

	        return usuarioRepository.save(usuario);
	    }

}
