package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.UsuarioResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.service.UsuarioService;

@Service
public class ListUsuariosUseCase {

    private final UsuarioService usuarioService;

    public ListUsuariosUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuariosPageResult execute(int page, int size) {
        Page<Usuario> pageResult = usuarioService.listarUsuariosComPaginacao(page, size);
        List<UsuarioResponseDTO> usuarios = pageResult.getContent().stream()
                .map(UsuarioResponseDTO::new)
                .toList();

        return new UsuariosPageResult(usuarios, pageResult.getTotalPages(), pageResult.getTotalElements());
    }
}
