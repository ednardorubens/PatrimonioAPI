create table marcas (
	id integer identity(1,1),
	nome varchar(100) not null,
	primary key (id),
	constraint marca_nome_uk unique (nome)
);