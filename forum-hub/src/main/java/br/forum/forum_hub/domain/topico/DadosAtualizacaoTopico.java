package br.forum.forum_hub.domain.topico;

public record DadosAtualizacaoTopico(
        String titulo,
        String mensagem,
        StatusTopico status
) {}
