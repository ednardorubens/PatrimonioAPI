package br.com.ermig.patrimonio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.ermig.patrimonio.dto.MarcaDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = true)
@Table(name = "marcas", uniqueConstraints = @UniqueConstraint(name = "marca_nome_uk", columnNames = { "nome" }))
public class Marca extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotBlank
	@Column(name = "nome", columnDefinition = "varchar(100)")
	private String nome;
	
	public Marca(final MarcaDTO marca) {
		if (marca != null) {
			this.id = marca.getId();
			this.nome = marca.getNome();
		}
	}

	public Marca(final String nome) {
		this.nome = nome;
	}

	public Marca(final Integer id, final String nome) {
		this.id = id;
		this.nome = nome;
	}

}
