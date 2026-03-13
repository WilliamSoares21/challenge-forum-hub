package br.forum.forum_hub.domain.topico;

import br.forum.forum_hub.domain.curso.CursoRepository;
import br.forum.forum_hub.domain.usuario.Usuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository, CursoRepository cursoRepository) {
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public Optional<DadosDetalheTopico> cadastrar(DadosCadastroTopico dados, Usuario usuarioLogado) {
        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return Optional.empty();
        }

        var curso = cursoRepository.findById(dados.idCurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        var topico = new Topico(dados, usuarioLogado, curso);
        topicoRepository.save(topico);

        return Optional.of(new DadosDetalheTopico(topico));
    }

    public Page<DadosListagemTopico> listar(Pageable paginacao) {
        return topicoRepository.findAll(paginacao).map(DadosListagemTopico::new);
    }

    public DadosDetalheTopico detalhar(Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        return new DadosDetalheTopico(topico);
    }

    @Transactional
    public DadosDetalheTopico atualizar(Long id, DadosAtualizacaoTopico dados) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        topico.atualizar(dados);
        return new DadosDetalheTopico(topico);
    }

    @Transactional
    public void excluir(Long id) {
        if (!topicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tópico não encontrado");
        }
        topicoRepository.deleteById(id);
    }
}
