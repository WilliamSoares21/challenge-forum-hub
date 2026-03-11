package br.forum.forum_hub.controller;

import br.forum.forum_hub.domain.curso.CursoRepository;
import br.forum.forum_hub.domain.topico.*;
import br.forum.forum_hub.domain.usuario.Usuario;
import br.forum.forum_hub.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalheTopico> cadastrar(
            @RequestBody @Valid DadosCadastroTopico dados,
            @AuthenticationPrincipal Usuario usuarioLogado,
            UriComponentsBuilder uriBuilder) {

        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.status(422).build();
        }

        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Curso não encontrado"));

        var topico = new Topico(dados, usuarioLogado, curso);
        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalheTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = topicoRepository.findAll(paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalheTopico> detalhar(@PathVariable Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Tópico não encontrado"));
        return ResponseEntity.ok(new DadosDetalheTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalheTopico> atualizar(
            @PathVariable Long id,
            @RequestBody DadosAtualizacaoTopico dados) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Tópico não encontrado"));
        topico.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalheTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            throw new jakarta.persistence.EntityNotFoundException("Tópico não encontrado");
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
