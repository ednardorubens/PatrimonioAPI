create table usuarios_perfis (
	usuario_id int not null,
	perfis_id int not null,
	constraint usuarios_perfis__perfil_fk foreign key (perfis_id) references perfis,
	constraint usuarios_perfis__usuario_fk foreign key (usuario_id) references usuarios
);

insert into usuarios_perfis values(1, 1);