create table usuarios_perfis_aud (
	rev int not null,
    usuario_id int not null,
    perfis_id int not null,
    revtype smallint,
    primary key (rev, usuario_id, perfis_id),
    constraint usuarios_perfis_aud_revinfo_fk foreign key (rev) references revinfo
);