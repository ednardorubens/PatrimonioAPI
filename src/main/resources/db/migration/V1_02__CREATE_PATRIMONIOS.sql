create table patrimonios (
	num_tombo integer identity(1,1),
	descricao varchar(255),
	nome varchar(100) not null,
	marca_id integer not null,
	data_atualizacao datetime,
	data_criacao datetime,
	criado_por varchar(255),
	atualizado_por varchar(255),
	primary key (num_tombo),
	constraint patrimonio_marca_fk foreign key (marca_id) references marcas
);