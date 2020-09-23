package br.com.navita.patrimonio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.com.navita.patrimonio.dto.PatrimonioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author eignacio
 *
 */
@Entity
@Getter
@Setter
@Audited
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patrimonios")
public class Patrimonio {

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

	public Patrimonio(final String nome, String descricao, final Marca marca) {
		this.nome = nome;
		this.descricao = descricao;
		this.marca = marca;
	}

	public Patrimonio(final PatrimonioDTO patrimonioDTO) {
		this(patrimonioDTO.getNome(), patrimonioDTO.getDescricao(), new Marca(patrimonioDTO.getMarca()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numTombo == null) ? 0 : numTombo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Patrimonio other = (Patrimonio) obj;
		if (numTombo == null) {
			if (other.numTombo != null) {
				return false;
			}
		} else if (!numTombo.equals(other.numTombo)) {
			return false;
		}
		return true;
	}

}
