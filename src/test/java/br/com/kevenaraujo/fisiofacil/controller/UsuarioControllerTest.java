package br.com.kevenaraujo.fisiofacil.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.kevenaraujo.fisiofacil.dto.UsuarioResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.EmailAlreadyRegisteredException;
import br.com.kevenaraujo.fisiofacil.exception.EmailSendException;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.exception.UnauthorizedException;
import br.com.kevenaraujo.fisiofacil.Configurations.JwtAuthenticationFilter;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.ListUsuariosUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.LoginResult;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.LoginUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.RequestPasswordResetUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.ResetPasswordUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.SignupUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.UsuariosPageResult;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignupUseCase signupUseCase;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private ListUsuariosUseCase listUsuariosUseCase;

    @MockBean
    private RequestPasswordResetUseCase requestPasswordResetUseCase;

    @MockBean
    private ResetPasswordUseCase resetPasswordUseCase;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void signupDeveRetornarConflictQuandoEmailJaExiste() throws Exception {
        when(signupUseCase.execute(any())).thenThrow(new EmailAlreadyRegisteredException("E-mail ja cadastrado"));

        String request = """
                {
                  "email":"existente@test.com",
                  "senha":"12345678",
                  "nomeUsuario":"joao"
                }
                """;

        mockMvc.perform(post("/api/usuarios/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("E-mail ja cadastrado"));
    }

    @Test
    void signupDeveRetornarCreatedQuandoCadastroForValido() throws Exception {
        Usuario salvo = new Usuario();
        salvo.setId(10L);
        when(signupUseCase.execute(any())).thenReturn(salvo);

        String request = """
                {
                  "email":"novo@test.com",
                  "senha":"12345678",
                  "nomeUsuario":"maria"
                }
                """;

        mockMvc.perform(post("/api/usuarios/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(10L));
    }

    @Test
    void loginDeveRetornarUnauthorizedQuandoUsuarioNaoExiste() throws Exception {
        when(loginUseCase.execute(any())).thenThrow(new UnauthorizedException("Usuario nao encontrado"));

        String request = """
                {
                  "email":"inexistente@test.com",
                  "password":"12345678"
                }
                """;

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Usuario nao encontrado"));
    }

    @Test
    void loginDeveRetornarOkQuandoCredenciaisForemValidas() throws Exception {
        when(loginUseCase.execute(any())).thenReturn(new LoginResult("token-123", "keven"));

        String request = """
                {
                  "email":"user@test.com",
                  "password":"12345678"
                }
                """;

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("keven"));
    }

    @Test
    void listarUsuariosNaoDeveExporSenha() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("u@test.com");
        usuario.setNomeUsuario("user");
        usuario.setSenha("segredo");
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setAtualizadoEm(LocalDateTime.now());

        UsuariosPageResult result = new UsuariosPageResult(
                List.of(new UsuarioResponseDTO(usuario)),
                1,
                1L);
        when(listUsuariosUseCase.execute(0, 10)).thenReturn(result);

        mockMvc.perform(get("/api/usuarios/listar?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarios[0].email").value("u@test.com"))
                .andExpect(jsonPath("$.usuarios[0].senha").doesNotExist());
    }

    @Test
    void esqueciSenhaDeveRetornarNotFoundQuandoUsuarioNaoExiste() throws Exception {
        doThrow(new ResourceNotFoundException("Usuario nao encontrado"))
                .when(requestPasswordResetUseCase).execute(any());
        String request = objectMapper.writeValueAsString(java.util.Map.of("email", "user@test.com"));

        mockMvc.perform(post("/api/usuarios/esqueci-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario nao encontrado"));
    }

    @Test
    void esqueciSenhaDeveRetornarOkQuandoUsuarioExiste() throws Exception {
        doNothing().when(requestPasswordResetUseCase).execute(any());
        String request = objectMapper.writeValueAsString(java.util.Map.of("email", "user@test.com"));

        mockMvc.perform(post("/api/usuarios/esqueci-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("E-mail de redefinicao de senha enviado"));
    }

    @Test
    void esqueciSenhaDeveRetornarInternalServerErrorQuandoFalharEnvioDeEmail() throws Exception {
        doThrow(new EmailSendException("Erro ao enviar o e-mail."))
                .when(requestPasswordResetUseCase).execute(any());
        String request = objectMapper.writeValueAsString(java.util.Map.of("email", "user@test.com"));

        mockMvc.perform(post("/api/usuarios/esqueci-senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro ao enviar o e-mail."));
    }
}
