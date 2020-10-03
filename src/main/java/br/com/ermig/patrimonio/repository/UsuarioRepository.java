package br.com.ermig.patrimonio.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ermig.patrimonio.model.Usuario;

public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);

}
