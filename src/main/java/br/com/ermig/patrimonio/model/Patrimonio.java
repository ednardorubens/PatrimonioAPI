package br.com.ermig.patrimonio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.ermig.patrimonio.dto.PatrimonioDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = { "numTombo" }, callSuper = true)
@Table(name = "patrimonios")
public class Patrimonio extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer numTombo;

	@NotNull
	@NotBlank
	private String nome;

	private String descricao;

	@NotNull
	@OneToOne
	private Marca marca;
	
	public Patrimonio(final String nome, final String descricao, final Marca marca) {
		this.nome = nome;
		this.descricao = descricao;
		this.marca = marca;
	}

	public Patrimonio(final PatrimonioDTO patrimonioDTO) {
		this(patrimonioDTO.getNome(), patrimonioDTO.getDescricao(), new Marca(patrimonioDTO.getMarca()));
	}

	public Patrimonio(final Integer numTombo, final String nome, final String descricao, final Marca marca) {
		this(nome, descricao, marca);
		this.numTombo = numTombo;
	}

}
