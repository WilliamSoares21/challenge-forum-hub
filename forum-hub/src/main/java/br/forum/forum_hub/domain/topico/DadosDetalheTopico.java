package br.forum.forum_hub.domain.topico;

import java.time.LocalDateTime;

public record DadosDetalheTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        StatusTopico status,
        String nomeAutor,
        String nomeCurso
) {
    public DadosDetalheTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                topico.getAutor().getNome(),
                topico.getCurso().getNome()
        );
    }
}
