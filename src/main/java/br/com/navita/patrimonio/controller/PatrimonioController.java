package br.com.navita.patrimonio.controller;

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

import br.com.navita.patrimonio.dto.MarcaDTO;
import br.com.navita.patrimonio.dto.PatrimonioDTO;
import br.com.navita.patrimonio.model.Patrimonio;
import br.com.navita.patrimonio.repository.MarcaRepository;
import br.com.navita.patrimonio.repository.PatrimonioRepository;

@RestController
@RequestMapping("/patrimonios")
public class PatrimonioController {

	@Autowired
	private MarcaRepository marcaRepository;

	@Autowired
	private PatrimonioRepository patrimonioRepository;

	@PostMapping
	@CacheEvict(value = "patrimonios", allEntries = true)
	public ResponseEntity<Patrimonio> salvar(@RequestBody @Valid final PatrimonioDTO patrimonioDTO, final UriComponentsBuilder uriBuilder) {
		final Patrimonio patrimonio = parse(patrimonioDTO);
		final Patrimonio patrimonioSalvo = this.patrimonioRepository.save(patrimonio);
		final URI uri = uriBuilder.path("/patrimonios/{id}").buildAndExpand(patrimonioSalvo.getNumTombo()).toUri();
		return ResponseEntity.created(uri).body(patrimonioSalvo);
	}

	@GetMapping
	@Cacheable(value = "patrimonios", key = "#id")
	public Page<Patrimonio> listar(@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) final Pageable pageable) {
		return this.patrimonioRepository.findAll(pageable);
	}

	@GetMapping("/{id}")
	@Cacheable(value = "patrimonios", key = "#id")
	public ResponseEntity<Patrimonio> buscar(@PathVariable final Integer id) {
		return this.processResponse(
			this.patrimonioRepository.findById(id),
			patrimonioRetorno -> ResponseEntity.ok(patrimonioRetorno)
		);
	}

	@PutMapping("/{id}")
	@CacheEvict(value = "patrimonios", allEntries = true)
	public ResponseEntity<Patrimonio> atualizar(@PathVariable final Integer id, @RequestBody @Valid final PatrimonioDTO patrimonioDTO) {
		final Patrimonio patrimonio = parse(patrimonioDTO);
		return this.processResponse(
			this.patrimonioRepository.findById(id).map(patrimonioDB -> {
				BeanUtils.copyProperties(patrimonio, patrimonioDB, "numTombo");
				return this.patrimonioRepository.save(patrimonioDB);
			}),
			patrimonioRetorno -> ResponseEntity.ok(patrimonioRetorno)
		);
	}

	@DeleteMapping("/{id}")
	@CacheEvict(value = "patrimonios", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable final Integer id) {
		return this.patrimonioRepository.findById(id).map(patrimonioDB -> {
			this.patrimonioRepository.delete(patrimonioDB);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	private ResponseEntity<Patrimonio> processResponse(final Optional<Patrimonio> optPatrimonio, final Function<Patrimonio, ResponseEntity<Patrimonio>> func) {
		return optPatrimonio.map(patrimonioRetorno -> func.apply(patrimonioRetorno)).orElse(ResponseEntity.notFound().build());
	}

	private Patrimonio parse(final PatrimonioDTO patrimonioDTO) {
		final Patrimonio patrimonio = new Patrimonio(patrimonioDTO);
		final MarcaDTO marcaDTO = patrimonioDTO.getMarca();
		if (marcaDTO != null && marcaDTO.getId() != null && marcaDTO.getId() > 0) {
			this.marcaRepository.findById(marcaDTO.getId())
				.ifPresent(marcaDB -> {
					patrimonio.setMarca(marcaDB);
				});
		}
		return patrimonio;
	}

}
