package br.com.kevenaraujo.fisiofacil.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.EmailSendException;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean validarSenha(String senhaDigitada, String senhaArmazenada) {
        return senhaDigitada != null && passwordEncoder.matches(senhaDigitada, senhaArmazenada);
    }

    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem, true);
            mailSender.send(mail);
        } catch (MessagingException e) {
            logger.error("Erro ao enviar e-mail para {}", destinatario, e);
            throw new EmailSendException("Erro ao enviar o e-mail.");
        }
    }

    @Async
    public void enviarEmailRedefinicaoAsync(Usuario usuario, String frontResetBaseUrl) {
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        String resetLink = frontResetBaseUrl + "?token=" + token;
        enviarEmail(
                usuario.getEmail(),
                "Redefinicao de Senha",
                "Clique no link para redefinir sua senha: " + resetLink);
    }

    public Optional<Usuario> buscarPorToken(String token) {
        return usuarioRepository.findByResetToken(token);
    }

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public Page<Usuario> listarUsuariosComPaginacao(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.findAll(pageable);
    }
}
