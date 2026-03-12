package br.forum.forum_hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.forum.forum_hub.domain.usuario.DadosCadastroUsuario;
import br.forum.forum_hub.domain.usuario.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados) {
        usuarioService.cadastrar(dados);
        return ResponseEntity.ok("Usuario cadastrado com sucesso");

    }

}
