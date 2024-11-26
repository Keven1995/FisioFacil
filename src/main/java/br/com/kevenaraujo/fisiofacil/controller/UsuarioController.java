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
@CrossOrigin(origins = "https://fisio-facil-front-nas6pg7kr-keven1995s-projects.vercel.app")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso com ID: " + novoUsuario.getId());
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
            "userName", usuario.getNomeUsuario()
        ));
    }
    

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}
