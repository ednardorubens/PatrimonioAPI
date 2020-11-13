package br.com.ermig.patrimonio.controller;

import static org.hamcrest.CoreMatchers.startsWith;
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

import br.com.ermig.patrimonio.model.Marca;
import br.com.ermig.patrimonio.repository.MarcaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MarcaControllerTest {

	private final Marca marca = new Marca(1, "samsung");

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MarcaRepository marcaRepository;

	@Test
	@WithMockUser("admin")
	void testErroSalvarMarca() throws Exception {
		when(this.marcaRepository.save(any(Marca.class))).thenReturn(this.marca);

		this.mvc.perform(
			post("/marcas")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errors").isNotEmpty())
		.andExpect(jsonPath("$.errors[0].name").value("nome"))
		.andExpect(jsonPath("$.errors[0].error").value(startsWith("must not be")))
		.andExpect(jsonPath("$.errors[1].name").value("nome"))
		.andExpect(jsonPath("$.errors[1].error").value(startsWith("must not be")));
	}

	@Test
	@WithMockUser("admin")
	void testSalvarMarca() throws Exception {
		when(this.marcaRepository.save(any(Marca.class))).thenReturn(this.marca);

		this.mvc.perform(
			post("/marcas")
			.content("{ \"nome\": \"samsung\" }")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(header().string("Location", "http://localhost/marcas/1"))
		.andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nome").value("samsung"));
	}

	@Test
	@WithMockUser("admin")
	void testListarMarca() throws Exception {
		when(this.marcaRepository.findAll(any(Pageable.class))).then(
			invocation -> {
				final Pageable pageable = (Pageable) invocation.getArguments()[0];
				return new PageImpl<>(List.of(this.marca), pageable, 1);
			}
		);

		this.mvc.perform(
			get("/marcas")
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content[0].id").value(1))
		.andExpect(jsonPath("$.content[0].nome").value("samsung"));
	}

	@Test
	@WithMockUser("admin")
	void testBuscarMarca() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));

		this.mvc.perform(
			get("/marcas/1")
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("samsung"));
	}

	@Test
	@WithMockUser("admin")
	void testAtualizarMarca() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));
		when(this.marcaRepository.save(any(Marca.class))).then(
			invocation -> (Marca) invocation.getArguments()[0]
		);

		this.mvc.perform(
			put("/marcas/1")
			.content("{ \"nome\": \"dell\" }")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("dell"));
	}

	@Test
	@WithMockUser("admin")
	void testRemoverMarca() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));

		this.mvc.perform(
			delete("/marcas/1")
		)
		.andExpect(status().isNoContent());
	}

}
