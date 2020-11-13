package br.com.ermig.patrimonio.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.ermig.patrimonio.model.Usuario;
import br.com.ermig.patrimonio.security.UsuarioDetailsService;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {
	
	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAware<String>() {
			
			@Autowired
			private UsuarioDetailsService usuarioDetailsService;
			
			@Override
			public Optional<String> getCurrentAuditor() {
				return Optional.ofNullable(SecurityContextHolder.getContext())
					.map(SecurityContext::getAuthentication)
					.filter(Authentication::isAuthenticated)
					.map(Authentication::getPrincipal)
					.map(Object::toString)
					.map(usuarioDetailsService::loadUserByUsername)
					.map(Usuario::toString);
			}
		};
	}
	
}
