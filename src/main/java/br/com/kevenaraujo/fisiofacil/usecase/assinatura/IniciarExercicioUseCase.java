package br.com.kevenaraujo.fisiofacil.usecase.assinatura;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioAssinatura;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioExercicioProgresso;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioAssinaturaRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioExercicioProgressoRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import br.com.kevenaraujo.fisiofacil.usecase.assinatura.AssinaturaExerciseCatalog.ExerciseDefinition;

@Service
public class IniciarExercicioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAssinaturaRepository usuarioAssinaturaRepository;
    private final UsuarioExercicioProgressoRepository usuarioExercicioProgressoRepository;
    private final AssinaturaExerciseCatalog exerciseCatalog;

    public IniciarExercicioUseCase(
            UsuarioRepository usuarioRepository,
            UsuarioAssinaturaRepository usuarioAssinaturaRepository,
            UsuarioExercicioProgressoRepository usuarioExercicioProgressoRepository,
            AssinaturaExerciseCatalog exerciseCatalog) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioAssinaturaRepository = usuarioAssinaturaRepository;
        this.usuarioExercicioProgressoRepository = usuarioExercicioProgressoRepository;
        this.exerciseCatalog = exerciseCatalog;
    }

    public void execute(String userEmail, String exercicioKey) {
        if (exercicioKey == null || exercicioKey.isBlank()) {
            throw new IllegalArgumentException("Exercicio e obrigatorio");
        }

        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        UsuarioAssinatura assinatura = usuarioAssinaturaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario ainda nao possui assinatura selecionada"));

        String membroSlug = exerciseCatalog.normalizeSlug(assinatura.getMembro().getNome());
        List<ExerciseDefinition> availableExercises = exerciseCatalog
                .listByPlanoAndMember(assinatura.getPlano().getNome(), membroSlug);

        ExerciseDefinition selectedExercise = availableExercises.stream()
                .filter(exercise -> exercise.key().equals(exercicioKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Exercicio indisponivel para a assinatura atual: " + exercicioKey));

        UsuarioExercicioProgresso progress = usuarioExercicioProgressoRepository
                .findByUsuarioIdAndExercicioKey(usuario.getId(), exercicioKey)
                .orElseGet(UsuarioExercicioProgresso::new);

        progress.setUsuario(usuario);
        progress.setExercicioKey(selectedExercise.key());
        progress.setExercicioTitulo(selectedExercise.titulo());
        usuarioExercicioProgressoRepository.save(progress);
    }
}
