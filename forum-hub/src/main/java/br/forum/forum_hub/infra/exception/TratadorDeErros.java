package br.forum.forum_hub.infra.exception;

import br.forum.forum_hub.domain.exception.RecursoNaoEncontradoException;
import br.forum.forum_hub.domain.exception.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> tratarErro404(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(criarProblemDetail(HttpStatus.NOT_FOUND, "RECURSO_NAO_ENCONTRADO", ex.getMessage(), request));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> tratarErroBadCredentials(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(criarProblemDetail(
                        HttpStatus.UNAUTHORIZED,
                        "CREDENCIAIS_INVALIDAS",
                        "Usuário inexistente ou senha inválida",
                        request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> tratarErro400(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var erros = ex.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        var problem = criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "ERRO_VALIDACAO",
                "Campos inválidos na requisição",
                request);
        problem.setProperty("erros", erros);
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ProblemDetail> tratarErroRegraDeNegocio(RegraDeNegocioException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(criarProblemDetail(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "REGRA_DE_NEGOCIO",
                        ex.getMessage(),
                        request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> tratarErroGenerico(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarProblemDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "ERRO_INTERNO",
                        "Erro interno inesperado",
                        request));
    }

    private ProblemDetail criarProblemDetail(HttpStatus status, String codigo, String mensagem, HttpServletRequest request) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, mensagem);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setProperty("codigo", codigo);
        problemDetail.setProperty("mensagem", mensagem);
        problemDetail.setProperty("timestamp", OffsetDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());
        return problemDetail;
    }

    private record DadosErroValidacao(String campo, String mensagem) {
        private DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
