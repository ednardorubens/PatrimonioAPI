create table perfis (
	id int identity(1,1),
	nome varchar(255) not null,
	primary key (id),
	constraint perfil_nome_uk unique (nome)
);