package br.com.kevenaraujo.fisiofacil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
