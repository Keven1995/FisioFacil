package br.com.kevenaraujo.fisiofacil.dto;

import jakarta.validation.constraints.NotBlank;

public record IniciarExercicioRequest(
        @NotBlank(message = "Exercicio e obrigatorio") String exercicioKey) {
}
