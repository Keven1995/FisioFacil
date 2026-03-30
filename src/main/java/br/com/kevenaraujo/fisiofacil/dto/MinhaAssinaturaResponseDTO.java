package br.com.kevenaraujo.fisiofacil.dto;

import java.util.List;

public record MinhaAssinaturaResponseDTO(
        Long usuarioId,
        String nomeUsuario,
        String email,
        String planoNome,
        String membroNome,
        String membroSlug,
        List<ExercicioAssinaturaDTO> exerciciosDisponiveis,
        List<ExercicioAssinaturaDTO> exerciciosEmAndamento,
        List<ExercicioAssinaturaDTO> exerciciosNaoAcessados) {
}
