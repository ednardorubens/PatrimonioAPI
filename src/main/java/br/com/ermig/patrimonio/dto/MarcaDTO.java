package br.com.ermig.patrimonio.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class MarcaDTO {

	private Integer id;

	@NotNull
	@NotBlank
	private String nome;
}
