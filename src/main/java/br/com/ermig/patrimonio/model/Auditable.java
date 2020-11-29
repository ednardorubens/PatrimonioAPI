package br.com.ermig.patrimonio.model;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedBy
	private String criadoPor;
	
	@LastModifiedBy
	private String atualizadoPor;

	@CreatedDate
	private LocalDateTime dataCriacao;
	
	@LastModifiedDate
	private LocalDateTime dataAtualizacao;

}