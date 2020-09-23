create table usuarios_aud (
	id int not null,
    rev int not null,
    revtype smallint,
    email varchar(255),
    nome varchar(255),
    senha varchar(255),
    primary key (id, rev),
    constraint usuarios_aud_revinfo_fk foreign key (rev) references revinfo
);