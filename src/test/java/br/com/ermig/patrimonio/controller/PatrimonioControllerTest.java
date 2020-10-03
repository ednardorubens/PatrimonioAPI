package br.com.ermig.patrimonio.controller;

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
import br.com.ermig.patrimonio.model.Patrimonio;
import br.com.ermig.patrimonio.repository.MarcaRepository;
import br.com.ermig.patrimonio.repository.PatrimonioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatrimonioControllerTest {

	private final Marca marca = new Marca(1, "monitor samsung");
	private final Patrimonio patrimonio = new Patrimonio(1, "monitor samsung", "monitor samsung", marca);

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MarcaRepository marcaRepository;

	@MockBean
	private PatrimonioRepository patrimonioRepository;

	@Test
	void testSalvarPatrimonio() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));
		when(this.patrimonioRepository.save(any(Patrimonio.class))).thenReturn(this.patrimonio);

		this.mvc.perform(
			post("/patrimonios")
			.content("{ \"nome\": \"monitor samsung\", \"descricao\": \"monitor samsung\", \"marca\": { \"id\": 1 }}")
			.contentType(MediaType.APPLICATION_JSON)
			.with(user("admin"))
		)
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(header().string("Location", "http://localhost/patrimonios/1"))
		.andExpect(jsonPath("$.numTombo").value(1))
        .andExpect(jsonPath("$.nome").value("monitor samsung"))
        .andExpect(jsonPath("$.descricao").value("monitor samsung"));
	}

	@Test
	void testListarPatrimonio() throws Exception {
		when(this.patrimonioRepository.findAll(any(Pageable.class))).then(
			invocation -> {
				final Pageable pageable = (Pageable) invocation.getArguments()[0];
				return new PageImpl<>(List.of(this.patrimonio), pageable, 1);
			}
		);

		this.mvc.perform(
			get("/patrimonios")
			.with(user("admin"))
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content[0].numTombo").value(1))
		.andExpect(jsonPath("$.content[0].nome").value("monitor samsung"))
		.andExpect(jsonPath("$.content[0].descricao").value("monitor samsung"));
	}

	@Test
	void testBuscarPatrimonio() throws Exception {
		when(this.patrimonioRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.patrimonio));

		this.mvc.perform(
			get("/patrimonios/1")
			.with(user("admin"))
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.numTombo").value(1))
		.andExpect(jsonPath("$.nome").value("monitor samsung"))
		.andExpect(jsonPath("$.descricao").value("monitor samsung"));
	}

	@Test
	void testAtualizarPatrimonio() throws Exception {
		when(this.marcaRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.marca));
		when(this.patrimonioRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.patrimonio));
		when(this.patrimonioRepository.save(any(Patrimonio.class))).then(
			invocation -> (Patrimonio) invocation.getArguments()[0]
		);

		this.mvc.perform(
			put("/patrimonios/1")
			.content("{ \"nome\": \"monitor dell\", \"marca\": { \"id\": 1 }}")
			.contentType(MediaType.APPLICATION_JSON)
			.with(user("admin"))
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.numTombo").value(1))
		.andExpect(jsonPath("$.nome").value("monitor dell"))
		.andExpect(jsonPath("$.descricao").isEmpty());
	}

	@Test
	void testRemoverPatrimonio() throws Exception {
		when(this.patrimonioRepository.findById(any(Integer.class))).thenReturn(Optional.of(this.patrimonio));

		this.mvc.perform(
			delete("/patrimonios/1")
			.with(user("admin"))
		)
		.andExpect(status().isNoContent());
	}

}
