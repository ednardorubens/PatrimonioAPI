create table marcas (
	id integer identity(1,1),
	nome varchar(100) not null,
	data_atualizacao datetime,
	data_criacao timestamp,
	criado_por varchar(255),
	atualizado_por varchar(255),
	primary key (id),
	constraint marca_nome_uk unique (nome)
);