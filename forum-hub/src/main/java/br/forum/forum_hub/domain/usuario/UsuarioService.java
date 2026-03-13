package br.forum.forum_hub.domain.usuario;

import br.forum.forum_hub.domain.exception.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario cadastrar(DadosCadastroUsuario dados){
        if (usuarioRepository.verificarEmailExistente(dados.email()) == true) {
            throw new RegraDeNegocioException("Usuário com e-mail " + dados.email() + " já existe");
        }
        var senhaHash = passwordEncoder.encode(dados.senha());
        var novoUsuario = new Usuario(dados, senhaHash);
        return usuarioRepository.save(novoUsuario);
    }
}
