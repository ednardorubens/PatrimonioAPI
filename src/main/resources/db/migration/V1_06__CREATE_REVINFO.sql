create table revinfo (
	id int identity(1,1),
	timestamp bigint not null,
	id_usuario int,
	nome_usuario varchar(255),
	primary key (id)
);