package br.com.ermig.patrimonio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ermig.patrimonio.dto.UsuarioDTO;
import br.com.ermig.patrimonio.model.Perfil;
import br.com.ermig.patrimonio.model.Usuario;
import br.com.ermig.patrimonio.repository.PerfilRepository;
import br.com.ermig.patrimonio.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerTest {

	private final UsuarioDTO usuarioDTO = new UsuarioDTO(1, "Raimundo Castro", "rcastro@empresa.com.br", "123", List.of("USER", "admin@patrimonio.com.br"));
	private final Usuario usuario = new Usuario(usuarioDTO);

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UsuarioRepository usuarioRepository;

	@MockBean
	private PerfilRepository perfilRepository;

	@Test
	@WithMockUser("admin@patrimonio.com.br")
	void testSalvarUsuario() throws Exception {
		when(this.perfilRepository.findByNome("admin@patrimonio.com.br")).thenReturn(Optional.of(new Perfil(1, "admin@patrimonio.com.br")));
		when(this.usuarioRepository.save(any(Usuario.class))).thenReturn(this.usuario);

		this.mvc.perform(
			post("/usuarios")
			.content(getJsonUsuario())
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(header().string("Location", "http://localhost/usuarios/1"))
		.andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nome").value("Raimundo Castro"))
		.andExpect(jsonPath("$.email").value("rcastro@empresa.com.br"))
		.andExpect(jsonPath("$.senha").doesNotExist());
	}

	@Test
	@WithMockUser("admin@patrimonio.com.br")
	void testListarUsuario() throws Exception {
		when(this.usuarioRepository.findAll(any(Pageable.class))).then(
			invocation -> {
				final Pageable pageable = (Pageable) invocation.getArguments()[0];
				return new PageImpl<>(List.of(this.usuario), pageable, 1);
			}
		);

		this.mvc.perform(
			get("/usuarios")
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content[0].id").value(1))
		.andExpect(jsonPath("$.content[0].nome").value("Raimundo Castro"))
		.andExpect(jsonPath("$.content[0].email").value("rcastro@empresa.com.br"))
		.andExpect(jsonPath("$.content[0].senha").doesNotExist());
	}

	@Test
	@WithMockUser("admin@patrimonio.com.br")
	void testBuscarUsuario() throws Exception {
		when(this.usuarioRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.usuario));

		this.mvc.perform(
			get("/usuarios/1")
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("Raimundo Castro"))
		.andExpect(jsonPath("$.email").value("rcastro@empresa.com.br"))
		.andExpect(jsonPath("$.senha").doesNotExist());
	}

	@Test
	@WithMockUser("admin@patrimonio.com.br")
	void testAtualizarUsuario() throws Exception {
		when(this.usuarioRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.usuario));
		when(this.usuarioRepository.save(any(Usuario.class))).then(
			invocation -> (Usuario) invocation.getArguments()[0]
		);

		this.mvc.perform(
			put("/usuarios/1")
			.content("{ \"nome\": \"Raimundo Nonato\" }")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("Raimundo Nonato"))
		.andExpect(jsonPath("$.senha").doesNotExist());


		this.mvc.perform(
			put("/usuarios/1")
			.content("{ \"nome\": \"Raimundo Nonato\", \"perfis\": [] }")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.perfis").isArray());
	}

	@Test
	@WithMockUser("admin@patrimonio.com.br")
	void testRemoverUsuario() throws Exception {
		when(this.usuarioRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.usuario));

		this.mvc.perform(
			delete("/usuarios/1")
		)
		.andExpect(status().isNoContent());
	}

	private String getJsonUsuario() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(usuarioDTO);
	}
}
