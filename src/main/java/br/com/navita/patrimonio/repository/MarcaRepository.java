package br.com.navita.patrimonio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.navita.patrimonio.model.Marca;

public interface MarcaRepository extends PagingAndSortingRepository<Marca, Integer> {

}
