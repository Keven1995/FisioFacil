package br.com.kevenaraujo.fisiofacil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.EmailSendException;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void salvarUsuarioDeveCriptografarSenhaAntesDeSalvar() {
        Usuario usuario = new Usuario();
        usuario.setSenha("12345678");
        when(passwordEncoder.encode("12345678")).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario salvo = usuarioService.salvarUsuario(usuario);
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        assertEquals("senha-criptografada", captor.getValue().getSenha());
        assertEquals("senha-criptografada", salvo.getSenha());
    }

    @Test
    void validarSenhaDeveRetornarTrueQuandoPasswordEncoderMatches() {
        when(passwordEncoder.matches("12345678", "hash")).thenReturn(true);

        boolean resultado = usuarioService.validarSenha("12345678", "hash");

        assertTrue(resultado);
    }

    @Test
    void validarSenhaDeveRetornarFalseQuandoSenhaDigitadaForNula() {
        boolean resultado = usuarioService.validarSenha(null, "hash");
        assertFalse(resultado);
    }

    @Test
    void buscarPorEmailDeveDelegarParaRepository() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@test.com");
        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("user@test.com");

        assertTrue(resultado.isPresent());
        assertEquals("user@test.com", resultado.get().getEmail());
    }

    @Test
    void listarUsuariosComPaginacaoDeveRetornarPageDoRepository() {
        Page<Usuario> page = new PageImpl<>(java.util.List.of(new Usuario()));
        when(usuarioRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Usuario> resultado = usuarioService.listarUsuariosComPaginacao(0, 10);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void atualizarUsuarioDeveDelegarParaRepository() {
        Usuario usuario = new Usuario();
        usuario.setEmail("atualizar@test.com");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario atualizado = usuarioService.atualizarUsuario(usuario);

        assertEquals("atualizar@test.com", atualizado.getEmail());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void buscarPorTokenDeveDelegarParaRepository() {
        Usuario usuario = new Usuario();
        usuario.setResetToken("token-abc");
        when(usuarioRepository.findByResetToken("token-abc")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorToken("token-abc");

        assertTrue(resultado.isPresent());
        assertEquals("token-abc", resultado.get().getResetToken());
    }

    @Test
    void criptografarSenhaDeveUsarPasswordEncoder() {
        when(passwordEncoder.encode("senha123")).thenReturn("hash-123");

        String hash = usuarioService.criptografarSenha("senha123");

        assertEquals("hash-123", hash);
    }

    @Test
    void enviarEmailDeveChamarMailSenderQuandoValido() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        usuarioService.enviarEmail("user@test.com", "Assunto", "Mensagem");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void enviarEmailDeveLancarEmailSendExceptionQuandoMensagemForInvalida() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThrows(EmailSendException.class,
                () -> usuarioService.enviarEmail("", "Assunto", "Mensagem"));
    }

    @Test
    void enviarEmailRedefinicaoAsyncDeveGerarTokenESalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("reset@test.com");

        UsuarioService spyService = Mockito.spy(new UsuarioService(usuarioRepository, mailSender, passwordEncoder));
        doNothing().when(spyService).enviarEmail(any(), any(), any());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        spyService.enviarEmailRedefinicaoAsync(usuario, "https://front/reset-password");

        assertNotNull(usuario.getResetToken());
        assertNotNull(usuario.getResetTokenExpiration());
        verify(usuarioRepository).save(usuario);
        verify(spyService).enviarEmail(
                eq("reset@test.com"),
                eq("Redefinicao de Senha"),
                contains("https://front/reset-password?token="));
    }
}
