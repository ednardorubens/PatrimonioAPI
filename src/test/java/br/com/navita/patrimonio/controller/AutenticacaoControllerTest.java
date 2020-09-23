package br.com.navita.patrimonio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import br.com.navita.patrimonio.dto.UsuarioDTO;
import br.com.navita.patrimonio.model.Usuario;
import br.com.navita.patrimonio.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class AutenticacaoControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public static String token;

	@Test
	@Order(1)
	void testAutenticacaoErro() throws Exception {
		this.mvc.perform(
			post("/auth")
			.content("{ \"email\": \"lcastro@empresa.com.br\", \"senha\": \"459\" }")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isUnauthorized());
	}

	@Test
	@Order(2)
	void testAutenticacaoSucesso() throws Exception {
		this.usuarioRepository.save(new Usuario(new UsuarioDTO("Raimundo Castro", "rcastro@empresa.com.br", "123", List.of("USER", "ADMIN"))));
		this.mvc.perform(
			post("/auth")
			.content("{ \"email\": \"rcastro@empresa.com.br\", \"senha\": \"123\" }")
			.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.tipo").value("Bearer"))
		.andExpect(jsonPath("$.token").isNotEmpty())
		.andDo(result -> setToken(result));
	}

	@Test
	@Order(3)
	void testAutenticacaoViaTokenGet() throws Exception {
		this.mvc.perform(
			get("/usuarios")
			.header("Authorization", "Bearer " + AutenticacaoControllerTest.token)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content[0].id").value(1))
		.andExpect(jsonPath("$.content[0].nome").value("Raimundo Castro"))
		.andExpect(jsonPath("$.content[0].email").value("rcastro@empresa.com.br"))
		.andExpect(jsonPath("$.content[0].senha").doesNotExist());
	}

	@Test
	@Order(4)
	void testAutenticacaoViaTokenPost() throws Exception {
		this.mvc.perform(
			post("/marcas")
			.content("{ \"nome\": \"samsung\" }")
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + AutenticacaoControllerTest.token)
		)
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(header().string("Location", "http://localhost/marcas/1"))
		.andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nome").value("samsung"));
	}

	@Test
	@Order(5)
	void testAutenticacaoViaTokenUnauthorized() throws Exception {
		this.mvc.perform(
			get("/usuarios")
		)
		.andExpect(status().isForbidden());

		this.mvc.perform(
			get("/usuarios")
			.header("Authorization", "usuario")
		)
		.andExpect(status().isForbidden());
	}

	private void setToken(final MvcResult result) throws UnsupportedEncodingException {
		AutenticacaoControllerTest.token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
	}
}
