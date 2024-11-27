package br.com.kevenaraujo.fisiofacil.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
@CrossOrigin(origins = "https://fisio-facil-front-nas6pg7kr-keven1995s-projects.vercel.app")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        // Verificar se o e-mail já está cadastrado
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "E-mail já cadastrado"));
        }

        // Verificar campos obrigatórios
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()
                || usuario.getSenha() == null || usuario.getSenha().isEmpty()
                || usuario.getNomeUsuario() == null || usuario.getNomeUsuario().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Todos os campos são obrigatórios"));
        }

        try {
            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuário cadastrado com sucesso",
                    "userId", novoUsuario.getId()));
        } catch (Exception e) {
            // Log do erro para análise
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao cadastrar usuário"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuário não encontrado"));
        }

        boolean senhaValida = usuarioService.validarSenha(loginRequest.getPassword(), usuario.getSenha());

        if (!senhaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Senha inválida"));
        }

        // Use um token gerado aleatoriamente para este contexto
        String token = UUID.randomUUID().toString();

        return ResponseEntity.ok(Map.of(
                "message", "Login bem-sucedido",
                "token", token,
                "userName", usuario.getNomeUsuario()));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Usuário não encontrado"));
        }

        // Gerar token de redefinição de senha
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        // Enviar e-mail com o link de redefinição de senha
        String resetLink = "https://fisio-facil-front.vercel.app/reset-password?token=" + token;
        usuarioService.enviarEmail(usuario.getEmail(), "Redefinição de Senha",
                "Clique no link para redefinir sua senha: " + resetLink);

        return ResponseEntity.ok(Map.of("message", "E-mail de redefinição de senha enviado"));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("novaSenha");

        // Buscar usuário pelo token usando Optional
        Optional<Usuario> usuarioOptional = usuarioRepository.findByResetToken(token);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token inválido ou expirado"));
        }

        Usuario usuario = usuarioOptional.get();

        // Atualizar a senha do usuário
        usuario.setSenha(usuarioService.criptografarSenha(novaSenha));
        usuario.setResetToken(null); // Limpar o token após redefinição
        usuario.setResetTokenExpiration(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso"));
    }

}
