package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.LoginRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para cadastro
    @PostMapping("/signup")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso com ID: " + novoUsuario.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Buscar o usuário pelo email
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuário não encontrado"));
        }

        // Validar a senha
        boolean senhaValida = usuarioService.validarSenha(loginRequest.getPassword(), usuario.getSenha());

        if (!senhaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Senha inválida"));
        }

        // Gerar um "token" simples, neste caso, um UUID aleatório
        String token = UUID.randomUUID().toString();

        // Retornar o token e o nome do usuário
        return ResponseEntity.ok(Map.of(
            "message", "Login bem-sucedido",
            "token", token,  // Token simples, pode ser uma chave única ou ID
            "userName", usuario.getNomeUsuario()  // Nome do usuário
        ));
    }

    // Método para listar todos os usuários
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}
