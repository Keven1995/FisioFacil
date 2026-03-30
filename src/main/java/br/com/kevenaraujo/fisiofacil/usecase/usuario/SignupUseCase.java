package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.SignupRequest;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.exception.EmailAlreadyRegisteredException;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@Service
public class SignupUseCase {

    private final UsuarioService usuarioService;

    public SignupUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario execute(SignupRequest request) {
        if (usuarioService.buscarPorEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("E-mail ja cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setNomeUsuario(request.getNomeUsuario());

        return usuarioService.salvarUsuario(usuario);
    }
}
