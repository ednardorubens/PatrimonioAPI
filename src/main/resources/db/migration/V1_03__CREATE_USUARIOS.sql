create table usuarios (
	id integer identity(1,1),
	email varchar(100) not null,
	nome varchar(100) not null,
	senha varchar(255) not null,
	primary key (id),
	constraint usuario_email_uk unique (email)
);

insert into usuarios(email, nome, senha) values('admin@patrimonio.com', 'admin', '{bcrypt}$2b$10$d/MTMJbWX8Y7paKERYQZJePvUBOz6tPxRKwUpAwk11zx6E.ySFiza');