package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.ForgotPasswordRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@Service
public class RequestPasswordResetUseCase {

    private final UsuarioService usuarioService;

    @Value("${app.reset-password.base-url:https://fisio-facil-front-end.vercel.app/reset-password}")
    private String resetPasswordBaseUrl;

    public RequestPasswordResetUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void execute(ForgotPasswordRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        usuarioService.enviarEmailRedefinicaoAsync(usuario, resetPasswordBaseUrl);
    }
}
