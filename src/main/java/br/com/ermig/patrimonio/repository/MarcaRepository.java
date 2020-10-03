package br.com.ermig.patrimonio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ermig.patrimonio.model.Marca;

public interface MarcaRepository extends PagingAndSortingRepository<Marca, Integer> {

}
