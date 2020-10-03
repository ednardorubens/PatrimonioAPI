package br.com.ermig.patrimonio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ermig.patrimonio.model.Patrimonio;

public interface PatrimonioRepository extends PagingAndSortingRepository<Patrimonio, Integer> {

}
