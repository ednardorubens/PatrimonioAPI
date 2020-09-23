package br.com.navita.patrimonio.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.navita.patrimonio.model.Patrimonio;

public interface PatrimonioRepository extends PagingAndSortingRepository<Patrimonio, Integer> {

}
