package br.forum.forum_hub.controller;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @NotBlank String login,
        @NotBlank String senha
) {}
