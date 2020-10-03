package br.com.ermig.patrimonio.controller;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
	void testErroSalvarMarca() throws Exception {
		when(this.marcaRepository.save(any(Marca.class))).thenReturn(this.marca);

		this.mvc.perform(
			post("/marcas")
			.content("{}")
			.contentType(MediaType.APPLICATION_JSON)
			.with(user("admin"))
		)
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.errors").isNotEmpty())
		.andExpect(jsonPath("$.errors[0].field").value("nome"))
		.andExpect(jsonPath("$.errors[0].error").value(startsWith("must not be")))
		.andExpect(jsonPath("$.errors[1].field").value("nome"))
		.andExpect(jsonPath("$.errors[1].error").value(startsWith("must not be")));
	}

	@Test
	void testSalvarMarca() throws Exception {
		when(this.marcaRepository.save(any(Marca.class))).thenReturn(this.marca);

		this.mvc.perform(
			post("/marcas")
			.content("{ \"nome\": \"samsung\" }")
			.contentType(MediaType.APPLICATION_JSON)
			.with(user("admin"))
		)
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(header().string("Location", "http://localhost/marcas/1"))
		.andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nome").value("samsung"));
	}

	@Test
	void testListarMarca() throws Exception {
		when(this.marcaRepository.findAll(any(Pageable.class))).then(
			invocation -> {
				final Pageable pageable = (Pageable) invocation.getArguments()[0];
				return new PageImpl<>(List.of(this.marca), pageable, 1);
			}
		);

		this.mvc.perform(
			get("/marcas")
			.with(user("admin"))
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content[0].id").value(1))
		.andExpect(jsonPath("$.content[0].nome").value("samsung"));
	}

	@Test
	void testBuscarMarca() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));

		this.mvc.perform(
			get("/marcas/1")
			.with(user("admin"))
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("samsung"));
	}

	@Test
	void testAtualizarMarca() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));
		when(this.marcaRepository.save(any(Marca.class))).then(
			invocation -> (Marca) invocation.getArguments()[0]
		);

		this.mvc.perform(
			put("/marcas/1")
			.content("{ \"nome\": \"dell\" }")
			.contentType(MediaType.APPLICATION_JSON)
			.with(user("admin"))
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("dell"));
	}

	@Test
	void testRemoverMarca() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));

		this.mvc.perform(
			delete("/marcas/1")
			.with(user("admin"))
		)
		.andExpect(status().isNoContent());
	}

}
