package br.com.navita.patrimonio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import br.com.navita.patrimonio.dto.MarcaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Audited
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "marcas", uniqueConstraints = @UniqueConstraint(name = "marca_nome_uk", columnNames = { "nome" }))
public class Marca {

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		final Marca other = (Marca) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
