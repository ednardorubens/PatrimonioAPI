package br.com.ermig.patrimonio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ermig.patrimonio.model.Usuario;
import br.com.ermig.patrimonio.security.UsuarioDetailsService;

@RestController
@RequestMapping("/oauth")
public class AutenticacaoController {
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;
	
	@GetMapping(path = "/user", produces = "application/json")
	public Optional<Usuario> user(final OAuth2Authentication user) {
		return Optional.ofNullable(user.getPrincipal())
			.map(Object::toString)
			.map(usuarioDetailsService::loadUserByUsername);
	}

}
