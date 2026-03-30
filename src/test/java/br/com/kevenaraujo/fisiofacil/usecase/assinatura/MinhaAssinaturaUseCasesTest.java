package br.com.kevenaraujo.fisiofacil.usecase.assinatura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.kevenaraujo.fisiofacil.dto.MinhaAssinaturaResponseDTO;
import br.com.kevenaraujo.fisiofacil.dto.SelecionarAssinaturaRequest;
import br.com.kevenaraujo.fisiofacil.entity.Membro;
import br.com.kevenaraujo.fisiofacil.entity.Planos;
import br.com.kevenaraujo.fisiofacil.entity.Usuario;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioAssinatura;
import br.com.kevenaraujo.fisiofacil.entity.UsuarioExercicioProgresso;
import br.com.kevenaraujo.fisiofacil.exception.ResourceNotFoundException;
import br.com.kevenaraujo.fisiofacil.repository.MembroRepository;
import br.com.kevenaraujo.fisiofacil.repository.PlanosRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioAssinaturaRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioExercicioProgressoRepository;
import br.com.kevenaraujo.fisiofacil.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class MinhaAssinaturaUseCasesTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PlanosRepository planosRepository;

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private UsuarioAssinaturaRepository usuarioAssinaturaRepository;

    @Mock
    private UsuarioExercicioProgressoRepository usuarioExercicioProgressoRepository;

    @Mock
    private AssinaturaExerciseCatalog assinaturaExerciseCatalog;

    @Test
    void selectMinhaAssinaturaDeveSalvarPlanoEMembroDoUsuario() {
        SelectMinhaAssinaturaUseCase useCase = new SelectMinhaAssinaturaUseCase(
                usuarioRepository,
                planosRepository,
                membroRepository,
                usuarioAssinaturaRepository);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("user@test.com");

        Planos plano = new Planos();
        plano.setNome("Basico");

        Membro membro = new Membro();
        membro.setNome("Cervical");

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(planosRepository.findFirstByNomeIgnoreCase("Basico")).thenReturn(Optional.of(plano));
        when(membroRepository.findFirstByNomeContainingIgnoreCase("Cervical")).thenReturn(Optional.of(membro));
        when(usuarioAssinaturaRepository.findByUsuarioId(1L)).thenReturn(Optional.empty());

        useCase.execute("user@test.com", new SelecionarAssinaturaRequest("Basico", "cervical"));

        ArgumentCaptor<UsuarioAssinatura> captor = ArgumentCaptor.forClass(UsuarioAssinatura.class);
        verify(usuarioAssinaturaRepository).save(captor.capture());
        assertEquals("Basico", captor.getValue().getPlano().getNome());
        assertEquals("Cervical", captor.getValue().getMembro().getNome());
    }

    @Test
    void getMinhaAssinaturaDeveRetornarExerciciosEmAndamentoENaoAcessados() {
        GetMinhaAssinaturaUseCase useCase = new GetMinhaAssinaturaUseCase(
                usuarioRepository,
                usuarioAssinaturaRepository,
                usuarioExercicioProgressoRepository,
                assinaturaExerciseCatalog);

        Usuario usuario = new Usuario();
        usuario.setId(7L);
        usuario.setEmail("user@test.com");
        usuario.setNomeUsuario("Lucas");

        Planos plano = new Planos();
        plano.setNome("Basico");

        Membro membro = new Membro();
        membro.setNome("Cervical");

        UsuarioAssinatura assinatura = new UsuarioAssinatura();
        assinatura.setUsuario(usuario);
        assinatura.setPlano(plano);
        assinatura.setMembro(membro);

        List<AssinaturaExerciseCatalog.ExerciseDefinition> available = List.of(
                new AssinaturaExerciseCatalog.ExerciseDefinition("cervical-flexao", "Flexao Cervical"),
                new AssinaturaExerciseCatalog.ExerciseDefinition("cervical-extensao", "Extensao Cervical"),
                new AssinaturaExerciseCatalog.ExerciseDefinition("cervical-rotacao", "Rotacao Cervical"));

        UsuarioExercicioProgresso progress = new UsuarioExercicioProgresso();
        progress.setExercicioKey("cervical-flexao");
        progress.setExercicioTitulo("Flexao Cervical");

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioAssinaturaRepository.findByUsuarioId(7L)).thenReturn(Optional.of(assinatura));
        when(assinaturaExerciseCatalog.normalizeSlug("Cervical")).thenReturn("cervical");
        when(assinaturaExerciseCatalog.listByPlanoAndMember("Basico", "cervical")).thenReturn(available);
        when(usuarioExercicioProgressoRepository.findByUsuarioIdAndExercicioKeyIn(
                7L,
                List.of("cervical-flexao", "cervical-extensao", "cervical-rotacao")))
                .thenReturn(List.of(progress));

        MinhaAssinaturaResponseDTO response = useCase.execute("user@test.com");

        assertEquals(3, response.exerciciosDisponiveis().size());
        assertEquals(1, response.exerciciosEmAndamento().size());
        assertEquals(2, response.exerciciosNaoAcessados().size());
    }

    @Test
    void iniciarExercicioDeveLancarErroQuandoExercicioNaoEstaDisponivelNoPlano() {
        IniciarExercicioUseCase useCase = new IniciarExercicioUseCase(
                usuarioRepository,
                usuarioAssinaturaRepository,
                usuarioExercicioProgressoRepository,
                assinaturaExerciseCatalog);

        Usuario usuario = new Usuario();
        usuario.setId(3L);
        usuario.setEmail("user@test.com");

        Planos plano = new Planos();
        plano.setNome("Basico");

        Membro membro = new Membro();
        membro.setNome("Cervical");

        UsuarioAssinatura assinatura = new UsuarioAssinatura();
        assinatura.setUsuario(usuario);
        assinatura.setPlano(plano);
        assinatura.setMembro(membro);

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioAssinaturaRepository.findByUsuarioId(3L)).thenReturn(Optional.of(assinatura));
        when(assinaturaExerciseCatalog.normalizeSlug("Cervical")).thenReturn("cervical");
        when(assinaturaExerciseCatalog.listByPlanoAndMember("Basico", "cervical")).thenReturn(List.of(
                new AssinaturaExerciseCatalog.ExerciseDefinition("cervical-flexao", "Flexao Cervical")));

        assertThrows(IllegalArgumentException.class, () -> useCase.execute("user@test.com", "cervical-rotacao"));
        verify(usuarioExercicioProgressoRepository, never()).save(any());
    }

    @Test
    void iniciarExercicioDeveSalvarProgressoQuandoExercicioEstaDisponivel() {
        IniciarExercicioUseCase useCase = new IniciarExercicioUseCase(
                usuarioRepository,
                usuarioAssinaturaRepository,
                usuarioExercicioProgressoRepository,
                assinaturaExerciseCatalog);

        Usuario usuario = new Usuario();
        usuario.setId(3L);
        usuario.setEmail("user@test.com");

        Planos plano = new Planos();
        plano.setNome("Plus");

        Membro membro = new Membro();
        membro.setNome("Cervical");

        UsuarioAssinatura assinatura = new UsuarioAssinatura();
        assinatura.setUsuario(usuario);
        assinatura.setPlano(plano);
        assinatura.setMembro(membro);

        when(usuarioRepository.findByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(usuarioAssinaturaRepository.findByUsuarioId(3L)).thenReturn(Optional.of(assinatura));
        when(assinaturaExerciseCatalog.normalizeSlug("Cervical")).thenReturn("cervical");
        when(assinaturaExerciseCatalog.listByPlanoAndMember("Plus", "cervical")).thenReturn(List.of(
                new AssinaturaExerciseCatalog.ExerciseDefinition("cervical-flexao", "Flexao Cervical")));
        when(usuarioExercicioProgressoRepository.findByUsuarioIdAndExercicioKey(3L, "cervical-flexao"))
                .thenReturn(Optional.empty());

        useCase.execute("user@test.com", "cervical-flexao");

        verify(usuarioExercicioProgressoRepository).save(any(UsuarioExercicioProgresso.class));
    }

    @Test
    void getMinhaAssinaturaDeveLancarNotFoundQuandoUsuarioNaoExiste() {
        GetMinhaAssinaturaUseCase useCase = new GetMinhaAssinaturaUseCase(
                usuarioRepository,
                usuarioAssinaturaRepository,
                usuarioExercicioProgressoRepository,
                assinaturaExerciseCatalog);

        when(usuarioRepository.findByEmail("inexistente@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute("inexistente@test.com"));
    }
}
