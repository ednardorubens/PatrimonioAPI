create table patrimonios_aud (
	num_tombo int not null,
    rev int not null,
    revtype tinyint,
    descricao varchar(255),
    nome varchar(255),
    marca_id int,
    primary key (num_tombo, rev),
    constraint patrimonios_aud_revinfo_fk foreign key (rev) references revinfo
);