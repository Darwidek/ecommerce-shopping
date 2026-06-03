package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.spring.ecommerce.model.Usuario;

/**
 * Service contract for managing users.
 *
 * Defines business operations related to user management,
 * including retrieval, persistence, and user registration.
 *
 * This abstraction keeps business logic decoupled from
 * the persistence layer.
 */
public interface IUsuarioService {

	/**
	 * Retrieves all users from the system.
	 *
	 * @return list of all users
	 */
	List<Usuario> findAll();

	/**
	 * Retrieves a user by its unique identifier.
	 *
	 * @param id user identifier
	 * @return Optional containing the user if found
	 */
	Optional<Usuario> findById(Integer id);

	/**
	 * Persists a user in the system.
	 *
	 * @param usuario user entity to be saved
	 * @return persisted user entity
	 */
	Usuario save(Usuario usuario);


	/**
	 * Retrieves a user by email address.
	 *
	 * @param email user email
	 * @return Optional containing the user if found
	 */
	Optional<Usuario> findByEmail(String email);

	/**
	 * Registers a new user in the system.
	 *
	 * This method may include additional business logic such as
	 * validation, password encoding, or role assignment.
	 *
	 * @param usuario user entity to register
	 * @return registered user entity
	 */
	public Usuario registrarUsuario(Usuario usuario);

}
