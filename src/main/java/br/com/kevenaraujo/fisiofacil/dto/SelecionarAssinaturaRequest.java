package br.com.kevenaraujo.fisiofacil.dto;

import jakarta.validation.constraints.NotBlank;

public record SelecionarAssinaturaRequest(
        @NotBlank(message = "Nome do plano e obrigatorio") String planoNome,
        @NotBlank(message = "Membro e obrigatorio") String membroSlug) {
}
