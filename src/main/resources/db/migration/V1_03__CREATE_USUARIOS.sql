create table usuarios (
	id integer identity(1,1),
	email varchar(100) not null,
	nome varchar(100) not null,
	senha varchar(255) not null,
	primary key (id),
	constraint usuario_email_uk unique (email)
);