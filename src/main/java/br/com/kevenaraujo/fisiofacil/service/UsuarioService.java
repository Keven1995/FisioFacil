package br.com.kevenaraujo.fisiofacil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final Map<String, String> tokensRedefinicaoSenha = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario salvarUsuario(Usuario usuario) {
        // Hash da senha ao salvar o usuário
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Validação da senha durante o login
    public boolean validarSenha(String senhaDigitada, String senhaArmazenada) {
        return passwordEncoder.matches(senhaDigitada, senhaArmazenada);
    }

    // Gerar token para redefinição de senha
    public String gerarTokenRedefinicaoSenha(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        tokensRedefinicaoSenha.put(token, usuario.getEmail()); // Associa o token ao email
        return token;
    }

    // Enviar email com link de redefinição de senha
    public void enviarEmailRedefinicaoSenha(String email, String token) {
        String linkRedefinicao = "http://seusistema.com/redefinir-senha?token=" + token;
        System.out.println("Link de redefinição enviado para " + email + ": " + linkRedefinicao);
        // Integre com um serviço de envio de e-mails (JavaMail, etc.)
    }

    // Validação do token
    public boolean validarTokenRedefinicao(String token) {
        return tokensRedefinicaoSenha.containsKey(token);
    }

    // Redefinir a senha com base no token
    public void redefinirSenha(String token, String novaSenha) {
        String email = tokensRedefinicaoSenha.get(token);
        if (email != null) {
            Usuario usuario = buscarPorEmail(email);
            usuario.setSenha(passwordEncoder.encode(novaSenha));
            salvarUsuario(usuario);
            tokensRedefinicaoSenha.remove(token);
        }
    }
    
}
