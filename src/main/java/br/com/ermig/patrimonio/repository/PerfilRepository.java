package br.com.ermig.patrimonio.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ermig.patrimonio.model.Perfil;

public interface PerfilRepository extends PagingAndSortingRepository<Perfil, Integer> {

	Optional<Perfil> findByNome(String nome);

}
