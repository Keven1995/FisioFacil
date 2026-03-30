package br.com.kevenaraujo.fisiofacil.usecase.assinatura;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import br.com.kevenaraujo.fisiofacil.dto.SelecionarAssinaturaRequest;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioAssinatura;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.repository.MembroRepository;
import br.com.kevenaraujo.fisiofacil.repository.PlanosRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioAssinaturaRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;

@Service
public class SelectMinhaAssinaturaUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PlanosRepository planosRepository;
    private final MembroRepository membroRepository;
    private final UsuarioAssinaturaRepository usuarioAssinaturaRepository;

    public SelectMinhaAssinaturaUseCase(
            UsuarioRepository usuarioRepository,
            PlanosRepository planosRepository,
            MembroRepository membroRepository,
            UsuarioAssinaturaRepository usuarioAssinaturaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.planosRepository = planosRepository;
        this.membroRepository = membroRepository;
        this.usuarioAssinaturaRepository = usuarioAssinaturaRepository;
    }

    public void execute(String userEmail, SelecionarAssinaturaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        String planoNomeNormalizado = normalizePlanoName(request.planoNome());
        Planos plano = planosRepository.findFirstByNomeIgnoreCase(planoNomeNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Plano nao encontrado: " + request.planoNome()));

        Membro membro = resolveMembroBySlug(request.membroSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Membro nao encontrado: " + request.membroSlug()));

        UsuarioAssinatura assinatura = usuarioAssinaturaRepository.findByUsuarioId(usuario.getId())
                .orElseGet(UsuarioAssinatura::new);

        assinatura.setUsuario(usuario);
        assinatura.setPlano(plano);
        assinatura.setMembro(membro);
        usuarioAssinaturaRepository.save(assinatura);
    }

    private String normalizePlanoName(String planoNome) {
        if (planoNome == null || planoNome.isBlank()) {
            throw new IllegalArgumentException("Nome do plano e obrigatorio");
        }

        String normalized = planoNome.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "basico", "básico" -> "Basico";
            case "intermediario", "intermediário" -> "Intermediario";
            case "plus" -> "Plus";
            default -> throw new IllegalArgumentException("Plano nao suportado: " + planoNome);
        };
    }

    private java.util.Optional<Membro> resolveMembroBySlug(String membroSlug) {
        String normalized = membroSlug == null ? "" : membroSlug.trim().toLowerCase(Locale.ROOT);

        List<String> aliases = switch (normalized) {
            case "cervical" -> List.of("Cervical");
            case "lombar" -> List.of("Lombar");
            case "sacral" -> List.of("Sacral");
            case "toracica" -> List.of("Toracica");
            case "joelho" -> List.of("Joelho");
            case "pe" -> List.of("Pe");
            case "tornozelo" -> List.of("Tornozelo");
            case "quadril" -> List.of("Quadril");
            case "punho" -> List.of("Punho");
            case "ombro" -> List.of("Ombro");
            case "mao" -> List.of("Mao");
            case "cotovelo" -> List.of("Cotovelo");
            default -> List.of(membroSlug);
        };

        for (String alias : aliases) {
            java.util.Optional<Membro> member = membroRepository.findFirstByNomeContainingIgnoreCase(alias);
            if (member.isPresent()) {
                return member;
            }
        }

        return java.util.Optional.empty();
    }
}
