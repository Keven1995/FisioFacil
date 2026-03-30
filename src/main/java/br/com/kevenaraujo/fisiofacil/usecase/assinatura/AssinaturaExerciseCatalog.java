package br.com.kevenaraujo.fisiofacil.usecase.assinatura;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AssinaturaExerciseCatalog {

    public record ExerciseDefinition(String key, String titulo) {
    }

    private static final Map<String, List<ExerciseDefinition>> EXERCISES_BY_MEMBER = Map.ofEntries(
            Map.entry("cervical", List.of(
                    new ExerciseDefinition("cervical-flexao", "Flexao Cervical"),
                    new ExerciseDefinition("cervical-extensao", "Extensao Cervical"),
                    new ExerciseDefinition("cervical-rotacao", "Rotacao Cervical"),
                    new ExerciseDefinition("cervical-inclinacao-lateral", "Inclinacao Lateral"),
                    new ExerciseDefinition("cervical-elevacao-ombros", "Elevacao de Ombros"))),
            Map.entry("lombar", List.of(
                    new ExerciseDefinition("lombar-flexao", "Flexao Lombar"),
                    new ExerciseDefinition("lombar-extensao", "Extensao Lombar"),
                    new ExerciseDefinition("lombar-rotacao", "Rotacao Lombar"),
                    new ExerciseDefinition("lombar-inclinacao-lateral", "Inclinacao Lateral Lombar"),
                    new ExerciseDefinition("lombar-ponte", "Ponte para Lombar"))),
            Map.entry("sacral", List.of(
                    new ExerciseDefinition("sacral-inclinacao-pelvica", "Inclinacao Pelvica"),
                    new ExerciseDefinition("sacral-ponte", "Ponte Pelvica"),
                    new ExerciseDefinition("sacral-alongamento-gluteos", "Alongamento de Gluteos"),
                    new ExerciseDefinition("sacral-mobilidade-quadril", "Mobilidade de Quadril"),
                    new ExerciseDefinition("sacral-fortalecimento-core", "Fortalecimento de Core"))),
            Map.entry("toracica", List.of(
                    new ExerciseDefinition("toracica-extensao", "Extensao Toracica"),
                    new ExerciseDefinition("toracica-rotacao", "Rotacao Toracica"),
                    new ExerciseDefinition("toracica-mobilidade-escapular", "Mobilidade Escapular"),
                    new ExerciseDefinition("toracica-abertura-peitoral", "Abertura Peitoral"),
                    new ExerciseDefinition("toracica-fortalecimento-postural", "Fortalecimento Postural"))),
            Map.entry("joelho", List.of(
                    new ExerciseDefinition("joelho-extensao", "Extensao de Joelho"),
                    new ExerciseDefinition("joelho-agachamento-parcial", "Agachamento Parcial"),
                    new ExerciseDefinition("joelho-elevacao-perna", "Elevacao de Perna Estendida"),
                    new ExerciseDefinition("joelho-mobilidade-faixa", "Mobilidade de Joelho com Faixa"),
                    new ExerciseDefinition("joelho-alongamento-isquiotibiais", "Alongamento de Isquiotibiais"))),
            Map.entry("pe", List.of(
                    new ExerciseDefinition("pe-flexao-plantar-dorsal", "Flexao Plantar e Dorsal"),
                    new ExerciseDefinition("pe-elevar-canhares", "Elevar Calcanhares"),
                    new ExerciseDefinition("pe-apanhar-toalha", "Apanhar Toalha com os Dedos"),
                    new ExerciseDefinition("pe-mobilidade-tornozelo", "Mobilidade de Tornozelo"),
                    new ExerciseDefinition("pe-alongamento-fascia", "Alongamento da Fascia Plantar"))),
            Map.entry("tornozelo", List.of(
                    new ExerciseDefinition("tornozelo-rotacao", "Rotacao de Tornozelo"),
                    new ExerciseDefinition("tornozelo-flexao-extensao", "Flexao e Extensao"),
                    new ExerciseDefinition("tornozelo-mobilidade-lateral", "Mobilidade Lateral"),
                    new ExerciseDefinition("tornozelo-fortalecimento-panturrilha", "Fortalecimento de Panturrilha"),
                    new ExerciseDefinition("tornozelo-alongamento-aquiles", "Alongamento de Aquiles"))),
            Map.entry("quadril", List.of(
                    new ExerciseDefinition("quadril-ponte-gluteos", "Ponte de Gluteos"),
                    new ExerciseDefinition("quadril-abducao", "Abducao de Quadril"),
                    new ExerciseDefinition("quadril-rotacao", "Rotacao de Quadril"),
                    new ExerciseDefinition("quadril-elevacao-lateral", "Elevacao Lateral da Perna"),
                    new ExerciseDefinition("quadril-alongamento-flexores", "Alongamento de Flexores"))),
            Map.entry("punho", List.of(
                    new ExerciseDefinition("punho-flexao", "Flexao de Punho"),
                    new ExerciseDefinition("punho-extensao", "Extensao de Punho"),
                    new ExerciseDefinition("punho-pronacao", "Pronacao de Punho"),
                    new ExerciseDefinition("punho-supinacao", "Supinacao de Punho"),
                    new ExerciseDefinition("punho-alongamento-flexores", "Alongamento de Flexores do Punho"))),
            Map.entry("ombro", List.of(
                    new ExerciseDefinition("ombro-rotacao-interna", "Rotacao Interna do Ombro"),
                    new ExerciseDefinition("ombro-rotacao-externa", "Rotacao Externa do Ombro"),
                    new ExerciseDefinition("ombro-elevacao-lateral", "Elevacao Lateral"),
                    new ExerciseDefinition("ombro-elevacao-frontal", "Elevacao Frontal"),
                    new ExerciseDefinition("ombro-alongamento-trapezio", "Alongamento de Trapezio"))),
            Map.entry("mao", List.of(
                    new ExerciseDefinition("mao-prensao-bola", "Prensao de Bola"),
                    new ExerciseDefinition("mao-extensao-dedos", "Extensao de Dedos"),
                    new ExerciseDefinition("mao-oposicao-polegar", "Oposicao do Polegar"),
                    new ExerciseDefinition("mao-deslizamento-tendao", "Deslizamento de Tendao"),
                    new ExerciseDefinition("mao-mobilidade-punho", "Mobilidade de Punho e Mao"))),
            Map.entry("cotovelo", List.of(
                    new ExerciseDefinition("cotovelo-flexao", "Flexao de Cotovelo"),
                    new ExerciseDefinition("cotovelo-extensao", "Extensao de Cotovelo"),
                    new ExerciseDefinition("cotovelo-pronacao", "Pronacao de Antebraco"),
                    new ExerciseDefinition("cotovelo-supinacao", "Supinacao de Antebraco"),
                    new ExerciseDefinition("cotovelo-alongamento-triceps", "Alongamento de Triceps"))));

    public List<ExerciseDefinition> listByPlanoAndMember(String planoNome, String membroSlug) {
        String normalizedSlug = normalizeSlug(membroSlug);
        List<ExerciseDefinition> exercises = EXERCISES_BY_MEMBER.get(normalizedSlug);
        if (exercises == null) {
            throw new IllegalArgumentException("Membro nao suportado: " + membroSlug);
        }

        PlanAccessPolicy policy = PlanAccessPolicy.fromPlanoNome(planoNome);
        return policy.limit(exercises);
    }

    public String normalizeSlug(String membroSlug) {
        if (membroSlug == null || membroSlug.isBlank()) {
            throw new IllegalArgumentException("Membro invalido");
        }
        return membroSlug.trim().toLowerCase(Locale.ROOT);
    }
}
