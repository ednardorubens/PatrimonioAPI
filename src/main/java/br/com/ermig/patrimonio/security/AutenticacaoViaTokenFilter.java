package br.com.ermig.patrimonio.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ermig.patrimonio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final FilterChain filterChain)
			throws ServletException, IOException {

		autenticarUsuario(request);

		filterChain.doFilter(request, response);
	}

	private void autenticarUsuario(final HttpServletRequest request) {
		final String auth = request.getHeader("Authorization");
		if (auth != null && auth.startsWith("Bearer ")) {
			final String token = auth.substring(7, auth.length());
			final Integer idUsuario = this.tokenService.getIdUsuario(token);
			this.usuarioRepository.findById(idUsuario).ifPresent(usuario -> {
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			});
		}
	}

}
