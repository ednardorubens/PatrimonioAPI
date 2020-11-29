package br.com.ermig.patrimonio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = { "id", "nome" })
@EqualsAndHashCode(of = { "id" })
@Table(name = "perfis", uniqueConstraints = @UniqueConstraint(name = "perfil_nome_uk", columnNames = { "nome" }))
public class Perfil implements GrantedAuthority {

	private static final long serialVersionUID = 8275301172711239698L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotBlank
	private String nome;

	@Override
	@JsonIgnore
	public String getAuthority() {
		return nome;
	}

	public Perfil(final String nome) {
		this.nome = nome;
	}

}
