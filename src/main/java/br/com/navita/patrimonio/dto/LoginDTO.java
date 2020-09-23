package br.com.navita.patrimonio.dto;

import javax.validation.constraints.NotNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

	@NotNull
	private String email;

	@NotNull
	private String senha;

	public UsernamePasswordAuthenticationToken getUserPass() {
		return new UsernamePasswordAuthenticationToken(this.email, this.senha);
	}

}
