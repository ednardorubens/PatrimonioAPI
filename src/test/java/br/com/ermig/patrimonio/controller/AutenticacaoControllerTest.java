package br.com.ermig.patrimonio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class AutenticacaoControllerTest {

	@Autowired
	private MockMvc mvc;

	public static String token;

	@Test
	@Order(1)
	void testAutenticacaoErro() throws Exception {
		this.mvc.perform(post("/usuarios"))
		.andExpect(status().isUnauthorized());
	}

	@Test
	@Order(2)
	@SuppressWarnings("deprecation")
	void testAutenticacaoSucesso() throws Exception {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "password");
	    params.add("username", "admin@patrimonio.com.br");
	    params.add("password", "P@7R1M0N10$");
		
		this.mvc.perform(
			post("/oauth/token")
			.header("Authorization", "Basic cGF0cmltb25pb19jbGllbnQ6UEA3UjFNME4xMCQ=")
			.params(params)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.token_type").value("bearer"))
		.andExpect(jsonPath("$.access_token").isNotEmpty())
		.andExpect(jsonPath("$.refresh_token").isNotEmpty())
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
		.andExpect(jsonPath("$.content[0].nome").value("admin"))
		.andExpect(jsonPath("$.content[0].email").value("admin@patrimonio.com.br"))
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
		.andExpect(status().isUnauthorized());

		this.mvc.perform(
			get("/usuarios")
			.header("Authorization", "usuario")
		)
		.andExpect(status().isUnauthorized());
	}

	private void setToken(final MvcResult result) throws UnsupportedEncodingException {
		AutenticacaoControllerTest.token = JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");
	}
}
