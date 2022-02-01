package br.org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.org.generation.blogpessoal.model.Usuario;
import br.org.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {

		// Requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", ""));

		// Enviando a requisição e recebendo uma resposta
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		// Checando se a resposta foi a resposta esperada
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

		// Checa se o nome do usuario foi enviado, foi gravado no banco de dados
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());

	}

	@Test
	@Order(2)
	@DisplayName("Não de ve permitir a duplicação do usuario")
	public void naoDeveDuplicarUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", ""));

		// Requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", ""));

		// Enviando a requisição e recebendo uma resposta
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		// Checando se a resposta foi a resposta esperada
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());

	}

	@Test
	@Order(3)
	@DisplayName("Atualizar Um Usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(
				new Usuario(0L, "Juliana Andrews ", "juliana_andrews@email.com.br", "juliana123", " "));

		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), "Juliana Andrews Ramos ",
				"juliana_ramos@email.com.br", "juliana123", " ");

		// Requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		// Enviando a requisição e recebendo uma resposta
		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		// Checando se a resposta foi a resposta esperada
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

		// Checa se o nome do usuario foi enviado, foi gravado no banco de dados
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());

	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosOsUsuarios() {

		usuarioService.cadastrarUsuario(
				new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", " "));

		usuarioService.cadastrarUsuario(
				new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", " "));

		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all",
				HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

}
