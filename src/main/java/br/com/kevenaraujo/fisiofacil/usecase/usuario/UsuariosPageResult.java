package br.com.kevenaraujo.fisiofacil.usecase.usuario;

import java.util.List;

import br.com.kevenaraujo.fisiofacil.dto.UsuarioResponseDTO;

public record UsuariosPageResult(List<UsuarioResponseDTO> usuarios, int totalPages, long totalUsers) {
}
