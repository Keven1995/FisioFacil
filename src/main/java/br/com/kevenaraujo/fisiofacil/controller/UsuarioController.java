package br.com.kevenaraujo.fisiofacil.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.kevenaraujo.fisiofacil.dto.ForgotPasswordRequest;
import br.com.kevenaraujo.fisiofacil.dto.LoginRequest;
import br.com.kevenaraujo.fisiofacil.dto.ResetPasswordRequest;
import br.com.kevenaraujo.fisiofacil.dto.SignupRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.ListUsuariosUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.LoginResult;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.LoginUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.RequestPasswordResetUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.ResetPasswordUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.SignupUseCase;
import br.com.kevenaraujo.fisiofacil.usecase.usuario.UsuariosPageResult;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final SignupUseCase signupUseCase;
    private final LoginUseCase loginUseCase;
    private final ListUsuariosUseCase listUsuariosUseCase;
    private final RequestPasswordResetUseCase requestPasswordResetUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public UsuarioController(
            SignupUseCase signupUseCase,
            LoginUseCase loginUseCase,
            ListUsuariosUseCase listUsuariosUseCase,
            RequestPasswordResetUseCase requestPasswordResetUseCase,
            ResetPasswordUseCase resetPasswordUseCase) {
        this.signupUseCase = signupUseCase;
        this.loginUseCase = loginUseCase;
        this.listUsuariosUseCase = listUsuariosUseCase;
        this.requestPasswordResetUseCase = requestPasswordResetUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody SignupRequest request) {
        Usuario novoUsuario = signupUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario cadastrado com sucesso",
                "userId", novoUsuario.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResult loginResult = loginUseCase.execute(loginRequest);
        return ResponseEntity.ok(Map.of(
                "message", "Login bem-sucedido",
                "token", loginResult.token(),
                "userName", loginResult.userName()));
    }

    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarTodosUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UsuariosPageResult result = listUsuariosUseCase.execute(page, size);
        return ResponseEntity.ok(Map.of(
                "usuarios", result.usuarios(),
                "totalPages", result.totalPages(),
                "totalUsers", result.totalUsers()));
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<?> esqueciSenha(@Valid @RequestBody ForgotPasswordRequest request) {
        requestPasswordResetUseCase.execute(request);
        return ResponseEntity.ok(Map.of("message", "E-mail de redefinicao de senha enviado"));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso"));
    }
}
