package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.LoginRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.UnauthorizedException;
import br.com.kevenaraujo.fisiofacil.service.JwtService;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@Service
public class LoginUseCase {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public LoginUseCase(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    public LoginResult execute(LoginRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Usuario nao encontrado"));

        boolean senhaValida = usuarioService.validarSenha(request.getPassword(), usuario.getSenha());
        if (!senhaValida) {
            throw new UnauthorizedException("Senha invalida");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", usuario.getNomeUsuario());
        if (usuario.getId() != null) {
            claims.put("userId", usuario.getId());
        }

        String token = jwtService.generateToken(usuario.getEmail(), claims);
        return new LoginResult(token, usuario.getNomeUsuario());
    }
}
