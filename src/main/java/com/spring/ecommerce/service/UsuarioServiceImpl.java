package com.spring.ecommerce.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private PasswordEncoder passwordEncoder; // Inyecta el encoder

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public List<Usuario> findAll() {

		return usuarioRepository.findAll();
	}
	
	 @Override
	    public Usuario registrarUsuario(Usuario usuario) {
	        // Encripta la contraseña antes de guardarla
	        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	        return usuarioRepository.save(usuario);
	    }

}
