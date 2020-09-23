create table marcas_aud (
	id int not null,
    rev int not null,
    revtype tinyint,
    nome varchar(255),
    primary key (id, rev),
    constraint marcas_aud_revinfo_fk foreign key (rev) references revinfo
);