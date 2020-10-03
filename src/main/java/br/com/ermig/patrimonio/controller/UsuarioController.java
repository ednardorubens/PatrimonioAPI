package br.com.ermig.patrimonio.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import br.com.ermig.patrimonio.dto.UsuarioDTO;
import br.com.ermig.patrimonio.model.Perfil;
import br.com.ermig.patrimonio.model.Usuario;
import br.com.ermig.patrimonio.repository.PerfilRepository;
import br.com.ermig.patrimonio.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@PostMapping
	@CacheEvict(value = "usuarios", allEntries = true)
	public ResponseEntity<Usuario> salvar(@RequestBody @Valid final UsuarioDTO usuarioDTO, final UriComponentsBuilder uriBuilder) {
		final Usuario usuario = parseUsuario(usuarioDTO);
		final Usuario usuarioSalvo = this.usuarioRepository.save(usuario);
		final URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioSalvo.getId()).toUri();
		return ResponseEntity.created(uri).body(usuarioSalvo);
	}

	@GetMapping
	@Cacheable(value = "usuarios", key = "#id")
	public Page<Usuario> listar(@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) final Pageable pageable) {
		return this.usuarioRepository.findAll(pageable);
	}

	@GetMapping("/{id}")
	@Cacheable(value = "usuarios", key = "#id")
	public ResponseEntity<Usuario> buscar(@PathVariable final Integer id) {
		return this.processResponse(
			this.usuarioRepository.findById(id),
			usuarioRetorno -> ResponseEntity.ok(usuarioRetorno)
		);
	}

	@PutMapping("/{id}")
	@CacheEvict(value = "usuarios", allEntries = true)
	public ResponseEntity<Usuario> atualizar(@PathVariable final Integer id, @RequestBody @Valid final UsuarioDTO usuarioDTO) {
		final Usuario usuario = parseUsuario(usuarioDTO);
		return this.processResponse(
			this.usuarioRepository.findById(id).map(usuarioDB -> {
				BeanUtils.copyProperties(usuario, usuarioDB, "id");
				return this.usuarioRepository.save(usuarioDB);
			}),
			usuarioRetorno -> ResponseEntity.ok(usuarioRetorno)
		);
	}

	@DeleteMapping("/{id}")
	@CacheEvict(value = "usuarios", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable final Integer id) {
		return this.usuarioRepository.findById(id).map(usuarioDB -> {
			this.usuarioRepository.delete(usuarioDB);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	private ResponseEntity<Usuario> processResponse(final Optional<Usuario> optUsuario, final Function<Usuario, ResponseEntity<Usuario>> func) {
		return optUsuario.map(usuarioRetorno -> func.apply(usuarioRetorno)).orElse(ResponseEntity.notFound().build());
	}

	private Usuario parseUsuario(final UsuarioDTO usuarioDTO) {
		final Usuario usuario = new Usuario(usuarioDTO);
		final List<String> perfisDTO = usuarioDTO.getPerfis();
		if (perfisDTO != null && !perfisDTO.isEmpty()) {
			usuario.getPerfis().addAll(perfisDTO.stream()
				.map(nomePerfil -> this.perfilRepository.findByNome(nomePerfil)
					.map(perfilDB -> perfilDB).orElse(new Perfil(nomePerfil))
				).collect(Collectors.toList())
			);
		}
		return usuario;
	}

}
