package br.com.kevenaraujo.fisiofacil.usecase.assinatura;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.ExercicioAssinaturaDTO;
import br.com.kevenaraujo.fisiofacil.dto.MinhaAssinaturaResponseDTO;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioAssinatura;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioExercicioProgresso;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioAssinaturaRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioExercicioProgressoRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;
import br.com.kevenaraujo.fisiofacil.usecase.assinatura.AssinaturaExerciseCatalog.ExerciseDefinition;

@Service
public class GetMinhaAssinaturaUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAssinaturaRepository usuarioAssinaturaRepository;
    private final UsuarioExercicioProgressoRepository usuarioExercicioProgressoRepository;
    private final AssinaturaExerciseCatalog exerciseCatalog;

    public GetMinhaAssinaturaUseCase(
            UsuarioRepository usuarioRepository,
            UsuarioAssinaturaRepository usuarioAssinaturaRepository,
            UsuarioExercicioProgressoRepository usuarioExercicioProgressoRepository,
            AssinaturaExerciseCatalog exerciseCatalog) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioAssinaturaRepository = usuarioAssinaturaRepository;
        this.usuarioExercicioProgressoRepository = usuarioExercicioProgressoRepository;
        this.exerciseCatalog = exerciseCatalog;
    }

    public MinhaAssinaturaResponseDTO execute(String userEmail) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        return usuarioAssinaturaRepository.findByUsuarioId(usuario.getId())
                .map(assinatura -> buildWithAssinatura(usuario, assinatura))
                .orElseGet(() -> buildWithoutAssinatura(usuario));
    }

    private MinhaAssinaturaResponseDTO buildWithoutAssinatura(Usuario usuario) {
        return new MinhaAssinaturaResponseDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                null,
                null,
                null,
                List.of(),
                List.of(),
                List.of());
    }

    private MinhaAssinaturaResponseDTO buildWithAssinatura(Usuario usuario, UsuarioAssinatura assinatura) {
        String planoNome = assinatura.getPlano().getNome();
        String membroSlug = exerciseCatalog.normalizeSlug(assinatura.getMembro().getNome());

        List<ExerciseDefinition> availableDefinitions = exerciseCatalog.listByPlanoAndMember(planoNome, membroSlug);
        List<String> availableKeys = availableDefinitions.stream()
                .map(ExerciseDefinition::key)
                .toList();

        Map<String, ExerciseDefinition> definitionByKey = availableDefinitions.stream()
                .collect(Collectors.toMap(ExerciseDefinition::key, Function.identity()));

        List<UsuarioExercicioProgresso> progressRows = usuarioExercicioProgressoRepository
                .findByUsuarioIdAndExercicioKeyIn(usuario.getId(), availableKeys);

        Set<String> inProgressKeys = progressRows.stream()
                .map(UsuarioExercicioProgresso::getExercicioKey)
                .collect(Collectors.toSet());

        List<ExercicioAssinaturaDTO> exerciciosDisponiveis = availableDefinitions.stream()
                .map(def -> new ExercicioAssinaturaDTO(def.key(), def.titulo()))
                .toList();

        List<ExercicioAssinaturaDTO> exerciciosEmAndamento = progressRows.stream()
                .map(row -> {
                    ExerciseDefinition definition = definitionByKey.get(row.getExercicioKey());
                    String title = definition != null ? definition.titulo() : row.getExercicioTitulo();
                    return new ExercicioAssinaturaDTO(row.getExercicioKey(), title);
                })
                .toList();

        List<ExercicioAssinaturaDTO> exerciciosNaoAcessados = availableDefinitions.stream()
                .filter(def -> !inProgressKeys.contains(def.key()))
                .map(def -> new ExercicioAssinaturaDTO(def.key(), def.titulo()))
                .toList();

        return new MinhaAssinaturaResponseDTO(
                usuario.getId(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                planoNome,
                assinatura.getMembro().getNome(),
                membroSlug,
                exerciciosDisponiveis,
                exerciciosEmAndamento,
                exerciciosNaoAcessados);
    }
}
