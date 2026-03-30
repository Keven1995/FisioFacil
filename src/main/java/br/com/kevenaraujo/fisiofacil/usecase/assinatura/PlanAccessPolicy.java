package br.com.kevenaraujo.fisiofacil.usecase.assinatura;

import java.util.List;
import java.util.Locale;

enum PlanAccessPolicy {
    BASICO(3),
    INTERMEDIARIO(5),
    PLUS(Integer.MAX_VALUE);

    private final int maxExercises;

    PlanAccessPolicy(int maxExercises) {
        this.maxExercises = maxExercises;
    }

    static PlanAccessPolicy fromPlanoNome(String planoNome) {
        if (planoNome == null || planoNome.isBlank()) {
            throw new IllegalArgumentException("Plano invalido");
        }

        String normalized = planoNome.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "basico" -> BASICO;
            case "intermediario" -> INTERMEDIARIO;
            case "plus" -> PLUS;
            default -> throw new IllegalArgumentException("Plano nao suportado: " + planoNome);
        };
    }

    <T> List<T> limit(List<T> exercises) {
        if (maxExercises >= exercises.size()) {
            return exercises;
        }
        return exercises.subList(0, maxExercises);
    }
}
