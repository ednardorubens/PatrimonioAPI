package br.com.navita.patrimonio.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.navita.patrimonio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);

}
