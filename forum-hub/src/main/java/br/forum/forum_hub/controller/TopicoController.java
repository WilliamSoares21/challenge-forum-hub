package br.forum.forum_hub.controller;

import br.forum.forum_hub.domain.topico.*;
import br.forum.forum_hub.domain.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    public ResponseEntity<DadosDetalheTopico> cadastrar(
            @RequestBody @Valid DadosCadastroTopico dados,
            @AuthenticationPrincipal Usuario usuarioLogado,
            UriComponentsBuilder uriBuilder) {

        var topicoCriado = topicoService.cadastrar(dados, usuarioLogado);
        if (topicoCriado.isEmpty()) {
            return ResponseEntity.status(422).build();
        }

        var detalhe = topicoCriado.get();
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(detalhe.id()).toUri();
        return ResponseEntity.created(uri).body(detalhe);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = topicoService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalheTopico> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(topicoService.detalhar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalheTopico> atualizar(
            @PathVariable Long id,
            @RequestBody DadosAtualizacaoTopico dados) {
        return ResponseEntity.ok(topicoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        topicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
