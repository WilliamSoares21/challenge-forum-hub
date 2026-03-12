package br.forum.forum_hub.domain.usuario;

import br.forum.forum_hub.controller.DadosAutenticacao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
        @NotBlank String nome,

        @NotBlank @Email String email,

        @NotBlank String senha) {
}
