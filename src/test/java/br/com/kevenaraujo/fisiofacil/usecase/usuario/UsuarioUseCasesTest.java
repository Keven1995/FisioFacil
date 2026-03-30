package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.kevenaraujo.fisiofacil.dto.ForgotPasswordRequest;
import br.com.kevenaraujo.fisiofacil.dto.LoginRequest;
import br.com.kevenaraujo.fisiofacil.dto.ResetPasswordRequest;
import br.com.kevenaraujo.fisiofacil.dto.SignupRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.EmailAlreadyRegisteredException;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.exception.UnauthorizedException;
import br.com.kevenaraujo.fisiofacil.service.JwtService;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCasesTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtService jwtService;

    @Test
    void signupUseCaseDeveLancarConflitoQuandoEmailExiste() {
        SignupUseCase useCase = new SignupUseCase(usuarioService);
        SignupRequest request = new SignupRequest();
        request.setEmail("existente@test.com");

        when(usuarioService.buscarPorEmail("existente@test.com")).thenReturn(Optional.of(new Usuario()));

        assertThrows(EmailAlreadyRegisteredException.class, () -> useCase.execute(request));
    }

    @Test
    void loginUseCaseDeveLancarUnauthorizedQuandoSenhaInvalida() {
        LoginUseCase useCase = new LoginUseCase(usuarioService, jwtService);
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("12345678");

        Usuario usuario = new Usuario();
        usuario.setSenha("hash");
        usuario.setNomeUsuario("user");
        when(usuarioService.buscarPorEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioService.validarSenha("12345678", "hash")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> useCase.execute(request));
    }

    @Test
    void loginUseCaseDeveGerarTokenJwtQuandoCredenciaisValidas() {
        LoginUseCase useCase = new LoginUseCase(usuarioService, jwtService);
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("12345678");

        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");
        usuario.setNomeUsuario("Keven");
        usuario.setSenha("hash");
        usuario.setId(9L);

        when(usuarioService.buscarPorEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioService.validarSenha("12345678", "hash")).thenReturn(true);
        when(jwtService.generateToken(any(String.class), any())).thenReturn("jwt-token");

        LoginResult resultado = useCase.execute(request);

        assertEquals("jwt-token", resultado.token());
        assertEquals("Keven", resultado.userName());
    }

    @Test
    void requestPasswordResetUseCaseDeveDispararEnvioEmail() {
        RequestPasswordResetUseCase useCase = new RequestPasswordResetUseCase(usuarioService);
        ReflectionTestUtils.setField(useCase, "resetPasswordBaseUrl", "https://front/reset-password");

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("user@test.com");
        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");

        when(usuarioService.buscarPorEmail("user@test.com")).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).enviarEmailRedefinicaoAsync(usuario, "https://front/reset-password");

        useCase.execute(request);

        verify(usuarioService).enviarEmailRedefinicaoAsync(usuario, "https://front/reset-password");
    }

    @Test
    void requestPasswordResetUseCaseDeveLancarNotFoundQuandoNaoExisteUsuario() {
        RequestPasswordResetUseCase useCase = new RequestPasswordResetUseCase(usuarioService);
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("inexistente@test.com");
        when(usuarioService.buscarPorEmail("inexistente@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(request));
    }

    @Test
    void resetPasswordUseCaseDeveAtualizarSenhaComSucesso() {
        ResetPasswordUseCase useCase = new ResetPasswordUseCase(usuarioService);
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("token-123");
        request.setNovaSenha("novaSenha123!");

        Usuario usuario = new Usuario();
        usuario.setResetToken("token-123");
        usuario.setResetTokenExpiration(LocalDateTime.now().plusMinutes(30));

        when(usuarioService.buscarPorToken("token-123")).thenReturn(Optional.of(usuario));
        when(usuarioService.criptografarSenha("novaSenha123!")).thenReturn("hash-novo");
        when(usuarioService.atualizarUsuario(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(request);

        assertEquals("hash-novo", usuario.getSenha());
        assertNull(usuario.getResetToken());
    }
}
