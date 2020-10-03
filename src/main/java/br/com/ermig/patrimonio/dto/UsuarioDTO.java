package br.com.ermig.patrimonio.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

	private Integer id;

	@NotNull
	@NotBlank
	private String nome;

	private String email;

	private String senha;

	private List<String> perfis;

	public UsuarioDTO(final String nome, final String email, final String senha, final List<String> perfis) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.perfis = perfis;
	}

}
