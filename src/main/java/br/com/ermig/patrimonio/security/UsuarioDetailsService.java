package br.com.ermig.patrimonio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.ermig.patrimonio.model.Usuario;
import br.com.ermig.patrimonio.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Usuario loadUserByUsername(final String username) throws UsernameNotFoundException {
		return this.usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Dados inválidos!"));
	}

}
