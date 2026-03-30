package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.ResetPasswordRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@Service
public class ResetPasswordUseCase {

    private final UsuarioService usuarioService;

    public ResetPasswordUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void execute(ResetPasswordRequest request) {
        Usuario usuario = usuarioService.buscarPorToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token invalido ou expirado"));

        if (usuario.getResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token invalido ou expirado");
        }

        usuario.setSenha(usuarioService.criptografarSenha(request.getNovaSenha()));
        usuario.setResetToken(null);
        usuario.setResetTokenExpiration(null);
        usuarioService.atualizarUsuario(usuario);
    }
}
