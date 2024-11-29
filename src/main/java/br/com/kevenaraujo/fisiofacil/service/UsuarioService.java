package br.com.kevenaraujo.fisiofacil.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public boolean validarSenha(String senhaDigitada, String senhaArmazenada) {
        return senhaDigitada != null && senhaDigitada.equals(senhaArmazenada);
    }

    /**
     * Gera um token de redefinição de senha para o usuário e o salva no banco.
     */
    public String gerarTokenRedefinicaoSenha(String email) {
        Usuario usuario = buscarPorEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o e-mail fornecido.");
        }

        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuarioRepository.save(usuario);
        return token;
    }

    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem, true); // Aceita HTML no conteúdo

            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar o e-mail.");
        }
    }

    public Optional<Usuario> buscarPorToken(String token) {
        return usuarioRepository.findByResetToken(token);
    }

    public void redefinirSenha(String token, String novaSenha) {
        Optional<Usuario> usuarioOptional = buscarPorToken(token);

        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setSenha(criptografarSenha(novaSenha));
        usuario.setResetToken(null); // Remove o token após o uso
        usuarioRepository.save(usuario);
    }

    public String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public Page<Usuario> listarUsuariosComPaginacao(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.findAll(pageable);
    }
}
