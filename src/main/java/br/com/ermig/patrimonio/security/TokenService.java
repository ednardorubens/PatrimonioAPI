package br.com.ermig.patrimonio.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.ermig.patrimonio.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expiration;

	public String gerarToken(Authentication authentication) {
		final Date hoje = new Date();
		final Usuario logado = (Usuario) authentication.getPrincipal();
		final Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

		return Jwts.builder()
			.setIssuer("Patrimônio API")
			.setSubject(String.valueOf(logado.getId()))
			.setIssuedAt(hoje)
			.setExpiration(dataExpiracao)
			.signWith(SignatureAlgorithm.HS256, this.secret)
			.compact();
	}

	public Integer getIdUsuario(final String token) {
		return Integer.valueOf(Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody().getSubject());
	}

}
