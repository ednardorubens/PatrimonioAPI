package br.com.ermig.patrimonio.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "REVINFO")
@RevisionEntity(AuditRevisionListener.class)
public class AuditRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = 3536361503645345441L;

	@Column(name = "id_usuario")
	private Integer idUsuario;

	@Column(name = "nome_usuario")
	private String nomeUsuario;

}
