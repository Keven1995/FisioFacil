package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para cadastro
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> cadastrarUsuario(@RequestBody Usuario usuario) {
        // Salva o usuário com senha hasheada (implementação no service)
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok(Map.of("message", "Usuário cadastrado com sucesso", "id", novoUsuario.getId().toString()));
    }

    // Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.buscarPorEmail(usuario.getEmail());

        if (usuarioExistente != null && usuarioService.validarSenha(usuario.getSenha(), usuarioExistente.getSenha())) {
            return ResponseEntity.ok(Map.of("message", "Login bem-sucedido"));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }

    // Método para listar todos os usuários
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Map<String, String>> solicitarRedefinicaoSenha(@RequestBody Map<String, String> payload) {
        String email = payload.get("email").trim();
        Usuario usuarioExistente = usuarioService.buscarPorEmail(email);

        if (usuarioExistente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        // Gerar token de redefinição (pode ser um UUID ou JWT)
        String token = usuarioService.gerarTokenRedefinicaoSenha(usuarioExistente);

        // Enviar email com o link de redefinição
        usuarioService.enviarEmailRedefinicaoSenha(usuarioExistente.getEmail(), token);

        return ResponseEntity.ok(Map.of("message", "Link de redefinição de senha enviado para o email."));
    }

    @GetMapping("/validar-token/{token}")
    public ResponseEntity<Map<String, String>> validarToken(@PathVariable String token) {
        boolean isValid = usuarioService.validarTokenRedefinicao(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido ou expirado");
        }

        return ResponseEntity.ok(Map.of("message", "Token válido"));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String novaSenha = payload.get("novaSenha");

        boolean isTokenValid = usuarioService.validarTokenRedefinicao(token);

        if (!isTokenValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido ou expirado");
        }

        // Redefinir a senha com hash
        usuarioService.redefinirSenha(token, novaSenha);

        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
    }

}
