package br.org.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.generation.blogpessoal.model.Postagem;
import br.org.generation.blogpessoal.repository.PostagemRepository;


@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins="*", allowedHeaders="*")
public class PostagemController {
	
	@Autowired
	private PostagemRepository postagemRepository;
	
	// Pucha todos os dados da tabela Postagem.
	@GetMapping
	public ResponseEntity <List<Postagem>> getAll(){
		
		return ResponseEntity.ok(postagemRepository.findAll());	
		// select * from tb_postagensS
	}
	
	
	// Chama o dado um por um pelo ID. Caso não exista retorna erro.
	@GetMapping("/{id}")
	public ResponseEntity <Postagem> GetById(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	// Chama os dados um por um pelo ID pelo modo if else.
	@GetMapping("/opcional/{id}")
	public ResponseEntity<Optional<Postagem>> getByOpcional(@PathVariable Long id){
		Optional <Postagem> resposta = postagemRepository.findById(id);
		
		if(resposta.isPresent()) 
			return ResponseEntity.ok(resposta);	
		else 
			return ResponseEntity.notFound().build();
		
	}
	// Chama todos os Ids que que contem a palavra pesquisada /metodo %like%.
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity <List<Postagem>> getByTitulo(@PathVariable String titulo){
		
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
			
		// select * from tb_postagens
	}
	// Inserindo o corpo das informações em JSON, cria se um novo Dado na tabela, .
	@PostMapping
	public ResponseEntity <Postagem> postPostagem(@Valid @RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
	}
	
	// Inserindo o corpo das informações em JSON, atualiza o Dado na tabela / S	em tratamento de erro.
	@PutMapping("/alterar")
	public ResponseEntity <Postagem> putAlterar(@Valid @RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
	}
	
	// Inserindo o corpo das informações em JSON, atualiza o Dado na tabela / Com tratamento de erro.
	@PutMapping
	public ResponseEntity <Postagem> putPostagem(@Valid @RequestBody Postagem postagem){
		//return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.ok(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	// Apaga o Dado da tabela pelo Id/ Com tratamento de erro
	@DeleteMapping("/{id}")
	public ResponseEntity <?> deletePostagem(@PathVariable Long id){
		return postagemRepository.findById(id)
				.map(resposta ->{
					postagemRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();	
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
}
