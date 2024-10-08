package br.com.kevenaraujo.fisiofacil.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Armazenamento temporário de tokens para redefinição (idealmente, no banco de dados)
    private final Map<String, String> tokensRedefinicaoSenha = new HashMap<>();

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Gerar token para redefinição de senha
    public String gerarTokenRedefinicaoSenha(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        tokensRedefinicaoSenha.put(token, usuario.getEmail()); // Armazena o token associado ao email
        return token;
    }

    // Enviar email com link de redefinição de senha
    public void enviarEmailRedefinicaoSenha(String email, String token) {
        // Implemente o envio de email (com JavaMail, por exemplo)
        String linkRedefinicao = "http://seusistema.com/redefinir-senha?token=" + token;
        System.out.println("Link de redefinição enviado para " + email + ": " + linkRedefinicao);
        // Aqui você pode integrar com um serviço de email real
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
            usuario.setSenha(novaSenha);
            salvarUsuario(usuario); // Atualiza a senha do usuário
            tokensRedefinicaoSenha.remove(token); // Remove o token após a redefinição
        }
    }
}
