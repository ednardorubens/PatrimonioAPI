package br.com.ermig.patrimonio.controller;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.ermig.patrimonio.dto.MarcaDTO;
import br.com.ermig.patrimonio.model.Marca;
import br.com.ermig.patrimonio.repository.MarcaRepository;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

	@Autowired
	private MarcaRepository marcaRepository;

	@PostMapping
	@CacheEvict(value = "marcas", allEntries = true)
	public ResponseEntity<Marca> salvar(@RequestBody @Valid final MarcaDTO marcaDTO, final UriComponentsBuilder uriBuilder) {
		final Marca marca = new Marca(marcaDTO.getNome());
		final Marca marcaSalvo = this.marcaRepository.save(marca);
		final URI uri = uriBuilder.path("/marcas/{id}").buildAndExpand(marcaSalvo.getId()).toUri();
		return ResponseEntity.created(uri).body(marcaSalvo);
	}

	@GetMapping
	@Cacheable(value = "marcas", key = "#id")
	public Page<Marca> listar(@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) final Pageable pageable) {
		return this.marcaRepository.findAll(pageable);
	}

	@GetMapping("/{id}")
	@Cacheable(value = "marcas", key = "#id")
	public ResponseEntity<Marca> buscar(@PathVariable final Integer id) {
		return this.processResponse(
			this.marcaRepository.findById(id),
			marcaRetorno -> ResponseEntity.ok(marcaRetorno)
		);
	}

	@PutMapping("/{id}")
	@CacheEvict(value = "marcas", allEntries = true)
	public ResponseEntity<Marca> atualizar(@PathVariable final Integer id, @RequestBody @Valid final MarcaDTO marcaDTO) {
		final Marca marca = new Marca(marcaDTO.getNome());
		return this.processResponse(
			this.marcaRepository.findById(id).map(marcaDB -> {
				BeanUtils.copyProperties(marca, marcaDB, "id");
				return this.marcaRepository.save(marcaDB);
			}),
			marcaRetorno -> ResponseEntity.ok(marcaRetorno)
		);
	}

	@DeleteMapping("/{id}")
	@CacheEvict(value = "marcas", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable final Integer id) {
		return this.marcaRepository.findById(id).map(marcaDB -> {
			this.marcaRepository.delete(marcaDB);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	private ResponseEntity<Marca> processResponse(final Optional<Marca> optMarca, final Function<Marca, ResponseEntity<Marca>> func) {
		return optMarca.map(marcaRetorno -> func.apply(marcaRetorno)).orElse(ResponseEntity.notFound().build());
	}

}
