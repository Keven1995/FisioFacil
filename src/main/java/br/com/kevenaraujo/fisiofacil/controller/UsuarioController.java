package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para cadastro
    @PostMapping("/signup")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.buscarPorEmail(usuario.getEmail());

        if (usuarioExistente != null && usuarioExistente.getSenha().equals(usuario.getSenha())) {
            return ResponseEntity.ok("Login bem-sucedido");
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    // Método para listar todos os usuários
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> solicitarRedefinicaoSenha(@RequestBody String email) {
        Usuario usuarioExistente = usuarioService.buscarPorEmail(email.trim());

        if (usuarioExistente == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        // Gerar token de redefinição (pode ser um UUID ou JWT)
        String token = usuarioService.gerarTokenRedefinicaoSenha(usuarioExistente);

        // Enviar email com o link de redefinição
        usuarioService.enviarEmailRedefinicaoSenha(usuarioExistente.getEmail(), token);

        return ResponseEntity.ok("Link de redefinição de senha enviado para o email.");
    }

    @GetMapping("/validar-token/{token}")
    public ResponseEntity<String> validarToken(@PathVariable String token) {
        boolean isValid = usuarioService.validarTokenRedefinicao(token);

        if (!isValid) {
            return ResponseEntity.status(400).body("Token inválido ou expirado");
        }

        return ResponseEntity.ok("Token válido");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String novaSenha = payload.get("novaSenha");

        boolean isTokenValid = usuarioService.validarTokenRedefinicao(token);

        if (!isTokenValid) {
            return ResponseEntity.status(400).body("Token inválido ou expirado");
        }

        // Redefinir a senha
        usuarioService.redefinirSenha(token, novaSenha);

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

}
