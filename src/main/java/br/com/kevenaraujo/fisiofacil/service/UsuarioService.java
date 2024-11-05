package br.com.kevenaraujo.fisiofacil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario(Usuario usuario) {
        // Salva o usuário sem hash da senha
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public boolean validarSenha(String senhaDigitada, String senhaArmazenada) {
        // Verifica se a senha digitada é nula ou vazia
        if (senhaDigitada == null || senhaDigitada.isEmpty()) {
            throw new IllegalArgumentException("Senha digitada não pode ser nula ou vazia");
        }
        // Compara a senha digitada com a senha armazenada
        return senhaDigitada.equals(senhaArmazenada);
    }
    
}
