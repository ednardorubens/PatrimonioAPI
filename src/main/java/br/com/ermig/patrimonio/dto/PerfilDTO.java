package br.com.ermig.patrimonio.dto;

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
public class PerfilDTO {

	private String nome;

}
