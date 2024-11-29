package br.com.kevenaraujo.fisiofacil.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.LoginRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Cadastrar usuário com validações e verificação de e-mail
    @PostMapping("/signup")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "E-mail já cadastrado"));
        }

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
            logger.error("Erro ao cadastrar usuário", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao cadastrar usuário"));
        }
    }

    // Login com validação de senha e criação de token
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

        String token = UUID.randomUUID().toString();

        return ResponseEntity.ok(Map.of(
                "message", "Login bem-sucedido",
                "token", token,
                "userName", usuario.getNomeUsuario()));
    }

    @GetMapping("/listar")
public ResponseEntity<Map<String, Object>> listarTodosUsuarios(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
    // Altere de List<Usuario> para Page<Usuario>
    Page<Usuario> pageResult = usuarioService.listarUsuariosComPaginacao(page, size);

    // A resposta inclui os dados da página, como o total de usuários, total de páginas e a lista de usuários
    Map<String, Object> response = Map.of(
        "usuarios", pageResult.getContent(), // getContent() retorna a lista de usuários
        "totalPages", pageResult.getTotalPages(), // Número total de páginas
        "totalUsers", pageResult.getTotalElements() // Número total de usuários
    );
    
    return ResponseEntity.ok(response);
}

    


    // Esqueci a senha - Envio de e-mail assíncrono
    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Usuário não encontrado"));
        }

        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiration(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        enviarEmailAsync(usuario, token);

        return ResponseEntity.ok(Map.of("message", "E-mail de redefinição de senha enviado"));
    }

    // Enviar e-mail assíncrono
    @Async
    public void enviarEmailAsync(Usuario usuario, String token) {
        String resetLink = "https://fisio-facil-front.vercel.app/reset-password?token=" + token;
        usuarioService.enviarEmail(usuario.getEmail(), "Redefinição de Senha",
                "Clique no link para redefinir sua senha: " + resetLink);
    }

    // Redefinir senha
    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String novaSenha = request.get("novaSenha");

        Optional<Usuario> usuarioOptional = usuarioRepository.findByResetToken(token);

        if (usuarioOptional.isEmpty() || usuarioOptional.get().getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token inválido ou expirado"));
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setSenha(usuarioService.criptografarSenha(novaSenha));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiration(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso"));
    }
}
